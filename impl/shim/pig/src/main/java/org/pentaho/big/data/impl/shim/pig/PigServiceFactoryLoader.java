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

package org.pentaho.big.data.impl.shim.pig;

import com.pentaho.big.data.bundles.impl.shim.common.ShimBridgingServiceTracker;
import org.osgi.framework.BundleContext;
import org.pentaho.bigdata.api.pig.PigServiceFactory;
import org.pentaho.di.core.hadoop.HadoopConfigurationBootstrap;
import org.pentaho.di.core.hadoop.HadoopConfigurationListener;
import org.pentaho.hadoop.shim.ConfigurationException;
import org.pentaho.hadoop.shim.HadoopConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by bryan on 7/6/15.
 */
public class PigServiceFactoryLoader implements HadoopConfigurationListener {
  private static final Logger LOGGER = LoggerFactory.getLogger( PigServiceFactoryLoader.class );
  private final BundleContext bundleContext;
  private final ShimBridgingServiceTracker shimBridgingServiceTracker;

  public PigServiceFactoryLoader( BundleContext bundleContext, ShimBridgingServiceTracker shimBridgingServiceTracker )
    throws ConfigurationException {
    this.bundleContext = bundleContext;
    this.shimBridgingServiceTracker = shimBridgingServiceTracker;
    HadoopConfigurationBootstrap.getInstance().registerHadoopConfigurationListener( this );
  }

  @Override public void onConfigurationOpen( HadoopConfiguration hadoopConfiguration, boolean defaultConfiguration ) {
    if ( hadoopConfiguration == null ) {
      return;
    }
    try {
      shimBridgingServiceTracker.registerWithClassloader( hadoopConfiguration, PigServiceFactory.class,
        PigServiceFactoryImpl.class.getCanonicalName(),
        bundleContext, hadoopConfiguration.getHadoopShim().getClass().getClassLoader(),
        new Class<?>[] { boolean.class, HadoopConfiguration.class },
        new Object[] { defaultConfiguration, hadoopConfiguration } );
    } catch ( Exception e ) {
      LOGGER.error( "Unable to register " + hadoopConfiguration.getIdentifier() + " shim", e );
    }
  }

  @Override public void onConfigurationClose( HadoopConfiguration hadoopConfiguration ) {
    shimBridgingServiceTracker.unregister( hadoopConfiguration );
  }

  @Override public void onClassLoaderAvailable( ClassLoader classLoader ) {
    // Noop
  }
}
