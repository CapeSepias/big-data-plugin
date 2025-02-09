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

package org.pentaho.big.data.impl.cluster.tests.oozie;

import org.pentaho.big.data.api.cluster.NamedCluster;
import org.pentaho.big.data.impl.cluster.tests.Constants;
import org.pentaho.runtime.test.i18n.MessageGetter;
import org.pentaho.runtime.test.i18n.MessageGetterFactory;
import org.pentaho.runtime.test.network.ConnectivityTestFactory;
import org.pentaho.runtime.test.result.RuntimeTestEntrySeverity;
import org.pentaho.runtime.test.result.RuntimeTestResultSummary;
import org.pentaho.runtime.test.result.org.pentaho.runtime.test.result.impl.RuntimeTestResultSummaryImpl;
import org.pentaho.runtime.test.test.impl.BaseRuntimeTest;
import org.pentaho.runtime.test.test.impl.RuntimeTestResultEntryImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;

/**
 * Created by bryan on 8/14/15.
 */
public class PingOozieHostTest extends BaseRuntimeTest {
  public static final String OOZIE_PING_OOZIE_HOST_TEST =
    "ooziePingOozieHostTest";
  public static final String PING_OOZIE_HOST_TEST_NAME = "PingOozieHostTest.Name";
  public static final String PING_OOZIE_HOST_TEST_MALFORMED_URL_DESC = "PingOozieHostTest.MalformedUrl.Desc";
  public static final String PING_OOZIE_HOST_TEST_MALFORMED_URL_MESSAGE = "PingOozieHostTest.MalformedUrl.Message";
  private static final Class<?> PKG = PingOozieHostTest.class;
  private final MessageGetterFactory messageGetterFactory;
  private final ConnectivityTestFactory connectivityTestFactory;
  private final MessageGetter messageGetter;

  public PingOozieHostTest( MessageGetterFactory messageGetterFactory,
                            ConnectivityTestFactory connectivityTestFactory ) {
    super( NamedCluster.class, Constants.OOZIE, OOZIE_PING_OOZIE_HOST_TEST,
      messageGetterFactory.create( PKG ).getMessage( PING_OOZIE_HOST_TEST_NAME ), new HashSet<String>() );
    this.messageGetterFactory = messageGetterFactory;
    this.messageGetter = messageGetterFactory.create( PKG );
    this.connectivityTestFactory = connectivityTestFactory;
  }

  @Override public RuntimeTestResultSummary runTest( Object objectUnderTest ) {
    // Safe to cast as our accepts method will only return true for named clusters
    NamedCluster namedCluster = (NamedCluster) objectUnderTest;
    String oozieUrl = namedCluster.getOozieUrl();
    try {
      URL url = new URL( oozieUrl );
      return  new RuntimeTestResultSummaryImpl( connectivityTestFactory
        .create( messageGetterFactory, url.getHost(), String.valueOf( url.getPort() ), false ).runTest() );
    } catch ( MalformedURLException e ) {
      return new RuntimeTestResultSummaryImpl( new RuntimeTestResultEntryImpl( RuntimeTestEntrySeverity.FATAL,
        messageGetter.getMessage( PING_OOZIE_HOST_TEST_MALFORMED_URL_DESC ),
        messageGetter.getMessage( PING_OOZIE_HOST_TEST_MALFORMED_URL_MESSAGE, oozieUrl ), e ) );
    }
  }
}
