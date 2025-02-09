/*******************************************************************************
 *
 * Pentaho Big Data
 *
 * Copyright (C) 2002-2015 by Pentaho : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package org.pentaho.runtime.test.network.impl;

import org.pentaho.di.core.Const;
import org.pentaho.runtime.test.i18n.MessageGetter;
import org.pentaho.runtime.test.i18n.MessageGetterFactory;
import org.pentaho.runtime.test.network.ConnectivityTest;
import org.pentaho.runtime.test.result.RuntimeTestEntrySeverity;
import org.pentaho.runtime.test.result.RuntimeTestResultEntry;
import org.pentaho.runtime.test.test.impl.RuntimeTestResultEntryImpl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bryan on 8/14/15.
 */
public class ConnectivityTestImpl implements ConnectivityTest {
  public static final String CONNECT_TEST_HOST_BLANK_DESC = "ConnectTest.HostBlank.Desc";
  public static final String CONNECT_TEST_HOST_BLANK_MESSAGE = "ConnectTest.HostBlank.Message";
  public static final String CONNECT_TEST_HA_DESC = "ConnectTest.HA.Desc";
  public static final String CONNECT_TEST_HA_MESSAGE = "ConnectTest.HA.Message";
  public static final String CONNECT_TEST_PORT_BLANK_DESC = "ConnectTest.PortBlank.Desc";
  public static final String CONNECT_TEST_PORT_BLANK_MESSAGE = "ConnectTest.PortBlank.Message";
  public static final String CONNECT_TEST_CONNECT_SUCCESS_DESC = "ConnectTest.ConnectSuccess.Desc";
  public static final String CONNECT_TEST_CONNECT_SUCCESS_MESSAGE = "ConnectTest.ConnectSuccess.Message";
  public static final String CONNECT_TEST_CONNECT_FAIL_DESC = "ConnectTest.ConnectFail.Desc";
  public static final String CONNECT_TEST_CONNECT_FAIL_MESSAGE = "ConnectTest.ConnectFail.Message";
  public static final String CONNECT_TEST_UNKNOWN_HOSTNAME_DESC = "ConnectTest.UnknownHostname.Desc";
  public static final String CONNECT_TEST_UNKNOWN_HOSTNAME_MESSAGE = "ConnectTest.UnknownHostname.Message";
  public static final String CONNECT_TEST_NETWORK_ERROR_DESC = "ConnectTest.NetworkError.Desc";
  public static final String CONNECT_TEST_NETWORK_ERROR_MESSAGE = "ConnectTest.NetworkError.Message";
  public static final String CONNECT_TEST_PORT_NUMBER_FORMAT_DESC = "ConnectTest.PortNumberFormat.Desc";
  public static final String CONNECT_TEST_PORT_NUMBER_FORMAT_MESSAGE = "ConnectTest.PortNumberFormat.Message";
  public static final String CONNECT_TEST_UNREACHABLE_DESC = "ConnectTest.Unreachable.Desc";
  public static final String CONNECT_TEST_UNREACHABLE_MESSAGE = "ConnectTest.Unreachable.Message";
  private static final Class<?> PKG = ConnectivityTestImpl.class;
  private final MessageGetter messageGetter;
  private final String hostname;
  private final String port;
  private final boolean haPossible;
  private final RuntimeTestEntrySeverity severityOfFalures;
  private final SocketFactory socketFactory;
  private final InetAddressFactory inetAddressFactory;

  public ConnectivityTestImpl( MessageGetterFactory messageGetterFactory, String hostname, String port,
                               boolean haPossible ) {
    this( messageGetterFactory, hostname, port, haPossible, RuntimeTestEntrySeverity.FATAL );
  }

  public ConnectivityTestImpl( MessageGetterFactory messageGetterFactory, String hostname, String port,
                               boolean haPossible,
                               RuntimeTestEntrySeverity severityOfFailures ) {
    this( messageGetterFactory, hostname, port, haPossible, severityOfFailures, new SocketFactory(),
      new InetAddressFactory() );
  }

  public ConnectivityTestImpl( MessageGetterFactory messageGetterFactory, String hostname, String port,
                               boolean haPossible,
                               RuntimeTestEntrySeverity severityOfFailures, SocketFactory socketFactory,
                               InetAddressFactory inetAddressFactory ) {
    this.messageGetter = messageGetterFactory.create( PKG );
    this.hostname = hostname;
    this.port = port;
    this.haPossible = haPossible;
    this.severityOfFalures = severityOfFailures;
    this.socketFactory = socketFactory;
    this.inetAddressFactory = inetAddressFactory;
  }

  @Override public RuntimeTestResultEntry runTest() {
    List<RuntimeTestResultEntry> runtimeTestResultEntries = new ArrayList<>();
    if ( Const.isEmpty( hostname ) ) {
      return new RuntimeTestResultEntryImpl( severityOfFalures,
        messageGetter.getMessage( CONNECT_TEST_HOST_BLANK_DESC ),
        messageGetter.getMessage( CONNECT_TEST_HOST_BLANK_MESSAGE ) );
    } else if ( Const.isEmpty( port ) ) {
      if ( haPossible ) {
        return new RuntimeTestResultEntryImpl( RuntimeTestEntrySeverity.INFO,
          messageGetter.getMessage( CONNECT_TEST_HA_DESC ),
          messageGetter.getMessage( CONNECT_TEST_HA_MESSAGE ) );
      } else {
        return new RuntimeTestResultEntryImpl( severityOfFalures,
          messageGetter.getMessage( CONNECT_TEST_PORT_BLANK_DESC ),
          messageGetter.getMessage( CONNECT_TEST_PORT_BLANK_MESSAGE ) );
      }
    } else {
      Socket socket = null;
      try {
        if ( inetAddressFactory.create( hostname ).isReachable( 10 * 1000 ) ) {
          try {
            socket = socketFactory.create( hostname, Integer.valueOf( port ) );
            return new RuntimeTestResultEntryImpl( RuntimeTestEntrySeverity.INFO,
              messageGetter.getMessage( CONNECT_TEST_CONNECT_SUCCESS_DESC ),
              messageGetter.getMessage( CONNECT_TEST_CONNECT_SUCCESS_MESSAGE, hostname, port ) );
          } catch ( IOException e ) {
            return new RuntimeTestResultEntryImpl( severityOfFalures,
              messageGetter.getMessage( CONNECT_TEST_CONNECT_FAIL_DESC ),
              messageGetter.getMessage( CONNECT_TEST_CONNECT_FAIL_MESSAGE, hostname, port ), e );
          } finally {
            if ( socket != null ) {
              try {
                socket.close();
              } catch ( IOException e ) {
                // Ignore
              }
            }
          }
        } else {
          return new RuntimeTestResultEntryImpl( severityOfFalures,
            messageGetter.getMessage( CONNECT_TEST_UNREACHABLE_DESC ),
            messageGetter.getMessage( CONNECT_TEST_UNREACHABLE_MESSAGE, hostname ) );
        }
      } catch ( UnknownHostException e ) {
        return new RuntimeTestResultEntryImpl( severityOfFalures,
          messageGetter.getMessage( CONNECT_TEST_UNKNOWN_HOSTNAME_DESC ),
          messageGetter.getMessage( CONNECT_TEST_UNKNOWN_HOSTNAME_MESSAGE, hostname ), e );
      } catch ( IOException e ) {
        return new RuntimeTestResultEntryImpl( severityOfFalures,
          messageGetter.getMessage( CONNECT_TEST_NETWORK_ERROR_DESC ),
          messageGetter.getMessage( CONNECT_TEST_NETWORK_ERROR_MESSAGE, hostname, port ), e );
      } catch ( NumberFormatException e ) {
        return new RuntimeTestResultEntryImpl( RuntimeTestEntrySeverity.FATAL,
          messageGetter.getMessage( CONNECT_TEST_PORT_NUMBER_FORMAT_DESC ),
          messageGetter.getMessage( CONNECT_TEST_PORT_NUMBER_FORMAT_MESSAGE ), e );
      }
    }
  }

  /**
   * Pulled out class to enable mock injection in tests
   */
  public static class SocketFactory {
    public Socket create( String hostname, int port ) throws IOException {
      return new Socket( hostname, port );
    }
  }

  /**
   * Pulled out class to enable mock injection in tests
   */
  public static class InetAddressFactory {
    public InetAddress create( String hostname ) throws UnknownHostException {
      return InetAddress.getByName( hostname );
    }
  }
}
