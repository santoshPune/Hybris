package com.hybris.yprofile.jalo;

import com.hybris.yprofile.constants.YprofileprocessadapterConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class YprofileprocessadapterManager extends GeneratedYprofileprocessadapterManager
{
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger( YprofileprocessadapterManager.class.getName() );
	
	public static final YprofileprocessadapterManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (YprofileprocessadapterManager) em.getExtension(YprofileprocessadapterConstants.EXTENSIONNAME);
	}
	
}
