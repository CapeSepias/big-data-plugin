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

package org.pentaho.big.data.plugins.common.ui.named.cluster.bridge;


import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.pentaho.big.data.api.cluster.NamedCluster;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.variables.VariableSpace;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for NamedClusterBridgeImpl. This is a bridge class to bridge NamedCluster objects from the legacy plugin
 * to OSGi.
 */
public class NamedClusterBridgeImplTest {
  private org.pentaho.di.core.namedcluster.model.NamedCluster legacyNamedCluster;
  private NamedClusterBridgeImpl namedClusterBridge;
  private String namedClusterName;
  private String hdfsHost;
  private String hdfsPort;
  private String hdfsUsername;
  private String hdfsPassword;
  private String jobTrackerHost;
  private String jobTrackerPort;
  private String zookeeperHost;
  private String zookeeperPort;
  private String oozieUrl;
  private boolean isMapr;
  private String toString;
  private long lastModifiedDate;
  private VariableSpace variableSpace;

  @Before
  public void setup() {
    namedClusterName = "namedClusterName";
    hdfsHost = "hdfsHost";
    hdfsPort = "hdfsPort";
    hdfsUsername = "hdfsUsername";
    hdfsPassword = "hdfsPassword";
    jobTrackerHost = "jobTrackerHost";
    jobTrackerPort = "jobTrackerPort";
    zookeeperHost = "zookeeperHost";
    zookeeperPort = "zookeeperPort";
    oozieUrl = "oozieUrl";
    isMapr = true;
    toString = "Named cluster: " + namedClusterName;
    lastModifiedDate = 11L;

    legacyNamedCluster = mock( org.pentaho.di.core.namedcluster.model.NamedCluster.class );
    namedClusterBridge = new NamedClusterBridgeImpl( legacyNamedCluster );
    variableSpace = mock( VariableSpace.class );
  }

  @Test
  public void testFromOsgiNamedCluster() {
    assertTrue( NamedClusterBridgeImpl.fromOsgiNamedCluster(
      mock( NamedCluster.class ) ) instanceof org.pentaho.di.core.namedcluster.model.NamedCluster );
    assertNull( NamedClusterBridgeImpl.fromOsgiNamedCluster( null ) );
  }

  @Test
  public void testGetName() {
    when( legacyNamedCluster.getName() ).thenReturn( namedClusterName );
    assertEquals( namedClusterName, namedClusterBridge.getName() );
  }

  @Test
  public void testSetName() {
    namedClusterBridge.setName( namedClusterName );
    verify( legacyNamedCluster ).setName( namedClusterName );
  }

  @Test
  public void testReplaceMeta() {
    NamedCluster namedCluster = mock( NamedCluster.class );

    when( namedCluster.getName() ).thenReturn( namedClusterName );
    when( namedCluster.getHdfsHost() ).thenReturn( hdfsHost );
    when( namedCluster.getHdfsPort() ).thenReturn( hdfsPort );
    when( namedCluster.getHdfsUsername() ).thenReturn( hdfsUsername );
    when( namedCluster.getHdfsPassword() ).thenReturn( hdfsPassword );
    when( namedCluster.getJobTrackerHost() ).thenReturn( jobTrackerHost );
    when( namedCluster.getJobTrackerPort() ).thenReturn( jobTrackerPort );
    when( namedCluster.getZooKeeperHost() ).thenReturn( zookeeperHost );
    when( namedCluster.getZooKeeperPort() ).thenReturn( zookeeperPort );
    when( namedCluster.getOozieUrl() ).thenReturn( oozieUrl );
    when( namedCluster.isMapr() ).thenReturn( isMapr );

    long before = System.currentTimeMillis();
    namedClusterBridge.replaceMeta( namedCluster );
    long after = System.currentTimeMillis();
    verify( legacyNamedCluster ).setName( namedClusterName );
    verify( legacyNamedCluster ).setHdfsHost( hdfsHost );
    verify( legacyNamedCluster ).setHdfsPort( hdfsPort );
    verify( legacyNamedCluster ).setHdfsUsername( hdfsUsername );
    verify( legacyNamedCluster ).setHdfsPassword( hdfsPassword );
    verify( legacyNamedCluster ).setJobTrackerHost( jobTrackerHost );
    verify( legacyNamedCluster ).setJobTrackerPort( jobTrackerPort );
    verify( legacyNamedCluster ).setZooKeeperHost( zookeeperHost );
    verify( legacyNamedCluster ).setZooKeeperPort( zookeeperPort );
    verify( legacyNamedCluster ).setOozieUrl( oozieUrl );
    verify( legacyNamedCluster ).setMapr( isMapr );
    ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass( long.class );
    verify( legacyNamedCluster ).setLastModifiedDate( argumentCaptor.capture() );
    Long modified = argumentCaptor.getValue();
    assertTrue( "Expected lastModified to be between start and end timestamps",
      modified >= before && modified <= after );
  }

  @Test
  public void testGetHdfsHost() {
    when( legacyNamedCluster.getHdfsHost() ).thenReturn( hdfsHost );
    assertEquals( hdfsHost, namedClusterBridge.getHdfsHost() );
  }

  @Test
  public void testSetHdfsHost() {
    namedClusterBridge.setHdfsHost( hdfsHost );
    verify( legacyNamedCluster ).setHdfsHost( hdfsHost );
  }

  @Test
  public void testGetHdfsPort() {
    when( legacyNamedCluster.getHdfsPort() ).thenReturn( hdfsPort );
    assertEquals( hdfsPort, namedClusterBridge.getHdfsPort() );
  }

  @Test
  public void testSetHdfsPort() {
    namedClusterBridge.setHdfsPort( hdfsPort );
    verify( legacyNamedCluster ).setHdfsPort( hdfsPort );
  }

  @Test
  public void testGetHdfsUsername() {
    when( legacyNamedCluster.getHdfsUsername() ).thenReturn( hdfsUsername );
    assertEquals( hdfsUsername, namedClusterBridge.getHdfsUsername() );
  }

  @Test
  public void testSetHdfsUsername() {
    namedClusterBridge.setHdfsUsername( hdfsUsername );
    verify( legacyNamedCluster ).setHdfsUsername( hdfsUsername );
  }

  @Test
  public void testGetHdfsPassword() {
    when( legacyNamedCluster.getHdfsPassword() ).thenReturn( hdfsPassword );
    assertEquals( hdfsPassword, namedClusterBridge.getHdfsPassword() );
  }

  @Test
  public void testSetHdfsPassword() {
    namedClusterBridge.setHdfsPassword( hdfsPassword );
    verify( legacyNamedCluster ).setHdfsPassword( hdfsPassword );
  }

  @Test
  public void testGetJobTrackerHost() {
    when( legacyNamedCluster.getJobTrackerHost() ).thenReturn( jobTrackerHost );
    assertEquals( jobTrackerHost, namedClusterBridge.getJobTrackerHost() );
  }

  @Test
  public void testSetJobTrackerHost() {
    namedClusterBridge.setJobTrackerHost( jobTrackerHost );
    verify( legacyNamedCluster ).setJobTrackerHost( jobTrackerHost );
  }

  @Test
  public void testGetJobTrackerPort() {
    when( legacyNamedCluster.getJobTrackerPort() ).thenReturn( jobTrackerPort );
    assertEquals( jobTrackerPort, namedClusterBridge.getJobTrackerPort() );
  }

  @Test
  public void testSetJobTrackerPort() {
    namedClusterBridge.setJobTrackerPort( jobTrackerPort );
    verify( legacyNamedCluster ).setJobTrackerPort( jobTrackerPort );
  }

  @Test
  public void testGetZookeeperHost() {
    when( legacyNamedCluster.getZooKeeperHost() ).thenReturn( zookeeperHost );
    assertEquals( zookeeperHost, namedClusterBridge.getZooKeeperHost() );
  }

  @Test
  public void testSetZookeeperHost() {
    namedClusterBridge.setZooKeeperHost( zookeeperHost );
    verify( legacyNamedCluster ).setZooKeeperHost( zookeeperHost );
  }

  @Test
  public void testGetZookeeperPort() {
    when( legacyNamedCluster.getZooKeeperPort() ).thenReturn( zookeeperPort );
    assertEquals( zookeeperPort, namedClusterBridge.getZooKeeperPort() );
  }

  @Test
  public void testSetZookeeperPort() {
    namedClusterBridge.setZooKeeperPort( zookeeperPort );
    verify( legacyNamedCluster ).setZooKeeperPort( zookeeperPort );
  }

  @Test
  public void testGetOozieUrl() {
    when( legacyNamedCluster.getOozieUrl() ).thenReturn( oozieUrl );
    assertEquals( oozieUrl, namedClusterBridge.getOozieUrl() );
  }

  @Test
  public void testSetOozieUrl() {
    namedClusterBridge.setOozieUrl( oozieUrl );
    verify( legacyNamedCluster ).setOozieUrl( oozieUrl );
  }

  @Test
  public void testGetLastModifiedDate() {
    when( legacyNamedCluster.getLastModifiedDate() ).thenReturn( lastModifiedDate );
    assertEquals( lastModifiedDate, namedClusterBridge.getLastModifiedDate() );
  }

  @Test
  public void testSetLastModifiedDate() {
    namedClusterBridge.setLastModifiedDate( lastModifiedDate );
    verify( legacyNamedCluster ).setLastModifiedDate( lastModifiedDate );
  }

  @Test
  public void testIsMapr() {
    when( legacyNamedCluster.isMapr() ).thenReturn( true ).thenReturn( false );
    assertTrue( namedClusterBridge.isMapr() );
    assertFalse( namedClusterBridge.isMapr() );
  }

  @Test
  public void testSetMapr() {
    namedClusterBridge.setMapr( true );
    verify( legacyNamedCluster ).setMapr( true );
  }

  @Test
  public void testClone() {
    org.pentaho.di.core.namedcluster.model.NamedCluster clone =
      mock( org.pentaho.di.core.namedcluster.model.NamedCluster.class );
    String cloneName = "cloneName";
    when( clone.getName() ).thenReturn( cloneName );
    when( legacyNamedCluster.clone() ).thenReturn( clone );
    assertEquals( cloneName, namedClusterBridge.clone().getName() );
  }

  @Test
  public void testInitializeVariablesFrom() {
    namedClusterBridge.initializeVariablesFrom( variableSpace );
    verify( legacyNamedCluster ).initializeVariablesFrom( variableSpace );
  }

  @Test
  public void testCopyVariablesFrom() {
    namedClusterBridge.copyVariablesFrom( variableSpace );
    verify( legacyNamedCluster ).copyVariablesFrom( variableSpace );
  }

  @Test
  public void testShareVariablesWith() {
    namedClusterBridge.shareVariablesWith( variableSpace );
    verify( legacyNamedCluster ).shareVariablesWith( variableSpace );
  }

  @Test
  public void testGetParentVariableSpace() {
    when( legacyNamedCluster.getParentVariableSpace() ).thenReturn( variableSpace );
    assertEquals( variableSpace, namedClusterBridge.getParentVariableSpace() );
  }

  @Test
  public void testSetParentVariableSpace() {
    namedClusterBridge.setParentVariableSpace( variableSpace );
    verify( legacyNamedCluster ).setParentVariableSpace( variableSpace );
  }

  @Test
  public void testSetVariable() {
    namedClusterBridge.setVariable( hdfsHost, hdfsPort );
    verify( legacyNamedCluster ).setVariable( hdfsHost, hdfsPort );
  }

  @Test
  public void testGetVariableDefault() {
    when( legacyNamedCluster.getVariable( hdfsHost, hdfsPort ) ).thenReturn( hdfsPassword );
    assertEquals( hdfsPassword, namedClusterBridge.getVariable( hdfsHost, hdfsPort ) );
  }

  @Test
  public void testGetVariable() {
    when( legacyNamedCluster.getVariable( hdfsHost ) ).thenReturn( hdfsPort );
    assertEquals( hdfsPort, namedClusterBridge.getVariable( hdfsHost ) );
  }

  @Test
  public void testGetBooleanValueOfVariable() {
    when( legacyNamedCluster.getBooleanValueOfVariable( hdfsHost, true ) ).thenReturn( false );
    assertEquals( false, namedClusterBridge.getBooleanValueOfVariable( hdfsHost, true ) );
  }

  @Test
  public void testListVariables() {
    String[] variables = { hdfsHost, hdfsPort, hdfsPassword, hdfsUsername };
    when( legacyNamedCluster.listVariables() ).thenReturn( variables );
    assertArrayEquals( variables, namedClusterBridge.listVariables() );
  }

  @Test
  public void testEnvironmentSubstitute() {
    when( legacyNamedCluster.environmentSubstitute( hdfsHost ) ).thenReturn( hdfsPort );
    assertEquals( hdfsPort, namedClusterBridge.environmentSubstitute( hdfsHost ) );
  }

  @Test
  public void testEnvironmentSubstituteArray() {
    String[] strings = { hdfsHost, hdfsPort, hdfsPassword, hdfsUsername };
    String[] result = { jobTrackerHost, jobTrackerPort, zookeeperHost, zookeeperPort };
    when( legacyNamedCluster.environmentSubstitute( strings ) ).thenReturn( result );
    assertArrayEquals( result, namedClusterBridge.environmentSubstitute( strings ) );
  }

  @SuppressWarnings( "unchecked" )
  @Test
  public void testInjectVariables() {
    Map<String, String> map = mock( Map.class );
    namedClusterBridge.injectVariables( map );
    verify( legacyNamedCluster ).injectVariables( map );
  }

  @Test
  public void testFieldSubstitute() throws KettleValueException {
    RowMetaInterface rowMetaInterface = mock( RowMetaInterface.class );
    Object[] objects = { hdfsHost, hdfsPort };
    when( legacyNamedCluster.fieldSubstitute( zookeeperHost, rowMetaInterface, objects ) ).thenReturn( oozieUrl );
    assertEquals( oozieUrl, namedClusterBridge.fieldSubstitute( zookeeperHost, rowMetaInterface, objects ) );
  }

  @Test
  public void testToString() {
    String namedClusterName = "namedClusterName";
    when( legacyNamedCluster.toString() ).thenReturn( "Named cluster: " + namedClusterName );
    assertEquals( "Named cluster: " + namedClusterName, namedClusterBridge.toString() );
  }
}
