/*******************************************************************************
 * Pentaho Big Data
 * <p/>
 * Copyright (C) 2002-2015 by Pentaho : http://www.pentaho.com
 * <p/>
 * ******************************************************************************
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package org.pentaho.big.data.impl.cluster;

import org.pentaho.big.data.api.cluster.NamedCluster;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.variables.Variables;
import org.pentaho.metastore.persist.MetaStoreAttribute;
import org.pentaho.metastore.persist.MetaStoreElementType;

import java.util.Map;

@MetaStoreElementType( name = "NamedCluster", description = "A NamedCluster" )
public class NamedClusterImpl implements NamedCluster {

  private VariableSpace variables = new Variables();

  @MetaStoreAttribute
  private String name;

  @MetaStoreAttribute
  private String hdfsHost;
  @MetaStoreAttribute
  private String hdfsPort;
  @MetaStoreAttribute
  private String hdfsUsername;
  @MetaStoreAttribute( password = true )
  private String hdfsPassword;

  @MetaStoreAttribute
  private String jobTrackerHost;
  @MetaStoreAttribute
  private String jobTrackerPort;

  @MetaStoreAttribute
  private String zooKeeperHost;
  @MetaStoreAttribute
  private String zooKeeperPort;

  @MetaStoreAttribute
  private String oozieUrl;

  @MetaStoreAttribute
  private boolean mapr;

  @MetaStoreAttribute
  private long lastModifiedDate = System.currentTimeMillis();



  public NamedClusterImpl() {
    initializeVariablesFrom( null );
  }

  public NamedClusterImpl( NamedCluster namedCluster ) {
    this();
    replaceMeta( namedCluster );
  }

  public void setName( String name ) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void copyVariablesFrom( VariableSpace space ) {
    variables.copyVariablesFrom( space );
  }

  public String environmentSubstitute( String aString ) {
    return variables.environmentSubstitute( aString );
  }

  public String[] environmentSubstitute( String[] aString ) {
    return variables.environmentSubstitute( aString );
  }

  public String fieldSubstitute( String aString, RowMetaInterface rowMeta, Object[] rowData )
    throws KettleValueException {
    return variables.fieldSubstitute( aString, rowMeta, rowData );
  }

  public VariableSpace getParentVariableSpace() {
    return variables.getParentVariableSpace();
  }

  public void setParentVariableSpace( VariableSpace parent ) {
    variables.setParentVariableSpace( parent );
  }

  public String getVariable( String variableName, String defaultValue ) {
    return variables.getVariable( variableName, defaultValue );
  }

  public String getVariable( String variableName ) {
    return variables.getVariable( variableName );
  }

  public boolean getBooleanValueOfVariable( String variableName, boolean defaultValue ) {
    if ( !Const.isEmpty( variableName ) ) {
      String value = environmentSubstitute( variableName );
      if ( !Const.isEmpty( value ) ) {
        return ValueMeta.convertStringToBoolean( value );
      }
    }
    return defaultValue;
  }

  public void initializeVariablesFrom( VariableSpace parent ) {
    variables.initializeVariablesFrom( parent );
  }

  public String[] listVariables() {
    return variables.listVariables();
  }

  public void setVariable( String variableName, String variableValue ) {
    variables.setVariable( variableName, variableValue );
  }

  public void shareVariablesWith( VariableSpace space ) {
    variables = space;
  }

  public void injectVariables( Map<String, String> prop ) {
    variables.injectVariables( prop );
  }

  public void replaceMeta( NamedCluster nc ) {
    this.setName( nc.getName() );
    this.setHdfsHost( nc.getHdfsHost() );
    this.setHdfsPort( nc.getHdfsPort() );
    this.setHdfsUsername( nc.getHdfsUsername() );
    this.setHdfsPassword( nc.getHdfsPassword() );
    this.setJobTrackerHost( nc.getJobTrackerHost() );
    this.setJobTrackerPort( nc.getJobTrackerPort() );
    this.setZooKeeperHost( nc.getZooKeeperHost() );
    this.setZooKeeperPort( nc.getZooKeeperPort() );
    this.setOozieUrl( nc.getOozieUrl() );
    this.setMapr( nc.isMapr() );
    this.lastModifiedDate = System.currentTimeMillis();
  }

  public NamedClusterImpl clone() {
    return new NamedClusterImpl( this );
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals( Object obj ) {
    if ( this == obj ) {
      return true;
    }
    if ( obj == null ) {
      return false;
    }
    if ( getClass() != obj.getClass() ) {
      return false;
    }
    NamedCluster other = (NamedCluster) obj;
    if ( name == null ) {
      if ( other.getName() != null ) {
        return false;
      }
    } else if ( !name.equals( other.getName() ) ) {
      return false;
    }
    return true;
  }

  public String getHdfsHost() {
    return hdfsHost;
  }

  public void setHdfsHost( String hdfsHost ) {
    this.hdfsHost = hdfsHost;
  }

  public String getHdfsPort() {
    return hdfsPort;
  }

  public void setHdfsPort( String hdfsPort ) {
    this.hdfsPort = hdfsPort;
  }

  public String getHdfsUsername() {
    return hdfsUsername;
  }

  public void setHdfsUsername( String hdfsUsername ) {
    this.hdfsUsername = hdfsUsername;
  }

  public String getHdfsPassword() {
    return hdfsPassword;
  }

  public void setHdfsPassword( String hdfsPassword ) {
    this.hdfsPassword = hdfsPassword;
  }

  public String getJobTrackerHost() {
    return jobTrackerHost;
  }

  public void setJobTrackerHost( String jobTrackerHost ) {
    this.jobTrackerHost = jobTrackerHost;
  }

  public String getJobTrackerPort() {
    return jobTrackerPort;
  }

  public void setJobTrackerPort( String jobTrackerPort ) {
    this.jobTrackerPort = jobTrackerPort;
  }

  public String getZooKeeperHost() {
    return zooKeeperHost;
  }

  public void setZooKeeperHost( String zooKeeperHost ) {
    this.zooKeeperHost = zooKeeperHost;
  }

  public String getZooKeeperPort() {
    return zooKeeperPort;
  }

  public void setZooKeeperPort( String zooKeeperPort ) {
    this.zooKeeperPort = zooKeeperPort;
  }

  public String getOozieUrl() {
    return oozieUrl;
  }

  public void setOozieUrl( String oozieUrl ) {
    this.oozieUrl = oozieUrl;
  }

  public long getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate( long lastModifiedDate ) {
    this.lastModifiedDate = lastModifiedDate;
  }

  public void setMapr( boolean mapr ) {
    this.mapr = mapr;
  }

  public boolean isMapr() {
    return mapr;
  }

  @Override
  public String toString() {
    return "Named cluster: " + getName();
  }
}
