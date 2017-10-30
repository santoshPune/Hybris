package de.hybris.platform.cmscockpit.session.impl;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmscockpit.events.impl.CmsNavigationEvent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class LiveEditBrowserAreaTest
{

	@Spy
	LiveEditBrowserArea browserArea;

	@Mock
	CmsNavigationEvent cmsNavigationEvent;

	@Mock
	LiveEditBrowserModel browserModel;

	@Test
	public void onCockpitEvent_calledWithCmsNavigationEvent_delegatesTo_setNavigationEventAttributes() throws Exception
	{
		// Arrange
		doReturn(browserModel).when(browserArea).getFocusedBrowser();

		// Act
		browserArea.onCockpitEvent(cmsNavigationEvent);

		// Assert
		verify(browserModel, times(1)).setNavigationEventAttributes(cmsNavigationEvent);
	}

}
