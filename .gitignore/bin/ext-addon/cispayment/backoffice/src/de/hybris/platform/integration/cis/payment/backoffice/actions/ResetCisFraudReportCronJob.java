package de.hybris.platform.integration.cis.payment.backoffice.actions;


import de.hybris.platform.integration.cis.payment.model.CisFraudReportCronJobModel;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import org.zkoss.zul.Messagebox;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationUtils;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;


public class ResetCisFraudReportCronJob implements CockpitAction<Object, Object>
{


	@Resource
	private ModelService modelService;

	@Override
	public ActionResult<Object> perform(final ActionContext<Object> actionContext)
	{
		final Object data = actionContext.getData();
		if (data instanceof CisFraudReportCronJobModel)
		{

			final CisFraudReportCronJobModel job = (CisFraudReportCronJobModel) data;

			job.setLastFraudReportEndTime(null);
			modelService.save(job);

			NotificationUtils.notifyUser(actionContext.getLabel("action.resetfraudreportendtimeaction.success"),
					NotificationEvent.Type.SUCCESS);
			return new ActionResult(ActionResult.SUCCESS, job);
		}

		Messagebox.show(data + " (" + ActionResult.ERROR + ")");
		return new ActionResult(ActionResult.ERROR);

	}

	@Override
	public boolean canPerform(final ActionContext<Object> actionContext)
	{
		final Object data = actionContext.getData();
		return data instanceof CisFraudReportCronJobModel;
	}

	@Override
	public boolean needsConfirmation(final ActionContext<Object> actionContext)
	{
		return true;
	}

	@Override
	public String getConfirmationMessage(final ActionContext<Object> actionContext)
	{
		return actionContext.getLabel("action.resetfraudreportendtimeaction.confirm");
	}
}