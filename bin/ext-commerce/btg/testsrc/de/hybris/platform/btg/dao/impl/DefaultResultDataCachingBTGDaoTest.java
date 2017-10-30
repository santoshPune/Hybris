/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 *
 *
 */
package de.hybris.platform.btg.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.btg.condition.impl.PriceExpressionEvaluator;
import de.hybris.platform.btg.enums.BTGConditionEvaluationScope;
import de.hybris.platform.btg.enums.BTGEvaluationMethod;
import de.hybris.platform.btg.enums.BTGResultScope;
import de.hybris.platform.btg.integration.BTGIntegrationTest;
import de.hybris.platform.btg.model.BTGAbstractResultModel;
import de.hybris.platform.btg.model.BTGConditionModel;
import de.hybris.platform.btg.model.BTGConditionResultModel;
import de.hybris.platform.btg.model.BTGExpressionModel;
import de.hybris.platform.btg.model.BTGOrderTotalSumOperandModel;
import de.hybris.platform.btg.model.BTGRuleModel;
import de.hybris.platform.btg.model.BTGRuleResultModel;
import de.hybris.platform.btg.model.BTGSegmentModel;
import de.hybris.platform.btg.model.BTGSegmentResultModel;
import de.hybris.platform.btg.services.impl.BTGEvaluationContext;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class DefaultResultDataCachingBTGDaoTest extends BTGIntegrationTest
{
	protected static final String SELECT_RESULT_FOR_SESSION = "SELECT {" + BTGAbstractResultModel.PK + "} FROM {"
			+ BTGAbstractResultModel._TYPECODE + "} WHERE {sessionId} = ?sessionId";

	@Resource(name = "defaultResultDataCachingBTGDao")
	private DefaultResultDataCachingBTGDao resultDataCachingBTGDao;
	@Resource
	private ModelService modelService;
	@Resource
	private SessionService sessionService;
	@Resource
	private FlexibleSearchService flexibleSearchService;


	@Test
	public void segmentInvalidationTest()
	{
		//given
		final String sessionId = sessionService.getCurrentSession().getSessionId();
		final BTGSegmentModel segment = createAndSaveSegment(BTGResultScope.SESSION);
		final BTGSegmentResultModel segmentResultModel = resultDataCachingBTGDao.createSegmentResult(customerA, segment,
				BTGResultScope.SESSION, false, sessionId, false);
		final boolean invalidated = setInvalidated(segmentResultModel);

		//when
		final boolean invalidatedResult = resultDataCachingBTGDao.isInvalidated(segmentResultModel);

		//then
		assertTrue(invalidatedResult == invalidated);
	}

	@Test
	public void ruleInvalidationTest()
	{
		//given
		final String sessionId = sessionService.getCurrentSession().getSessionId();
		final BTGRuleModel rule = createAndSaveRule(BTGResultScope.SESSION);
		final BTGRuleResultModel ruleResultModel = resultDataCachingBTGDao.createRuleResult(customerA, rule,
				BTGResultScope.SESSION, false, sessionId, false);
		final boolean invalidated = setInvalidated(ruleResultModel);

		//when
		final boolean invalidatedResult = resultDataCachingBTGDao.isInvalidated(ruleResultModel);

		//then
		assertTrue(invalidatedResult == invalidated);
	}

	@Test
	public void conditionInvalidationTest()
	{
		//given
		final String sessionId = sessionService.getCurrentSession().getSessionId();
		final BTGExpressionModel condition = (BTGExpressionModel) createAndSaveCondition(BTGResultScope.SESSION);
		final BTGConditionResultModel conditionResultModel = resultDataCachingBTGDao.createConditionResult(customerA, condition,
				BTGResultScope.SESSION, false, sessionId, false);
		final boolean invalidated = setInvalidated(conditionResultModel);

		//when
		final boolean invalidatedResult = resultDataCachingBTGDao.isInvalidated(conditionResultModel);

		//then
		assertTrue(invalidatedResult == invalidated);
	}

	@Test
	public void invalidationWhenNoCacheEntryExistsTest()
	{
		//given
		final String sessionId = sessionService.getCurrentSession().getSessionId();
		final BTGSegmentModel segment = createAndSaveSegment(BTGResultScope.SESSION);
		final BTGSegmentResultModel notCachedResultModel = createSegmentResultModel(customerA, segment, BTGResultScope.SESSION,
				false, sessionId, false, false);

		//when
		final boolean invalidated = resultDataCachingBTGDao.isInvalidated(notCachedResultModel);

		//then
		assertTrue(resultDataCachingBTGDao.isInvalidatedWhenNoCacheEntryExists() == invalidated);
	}

	@Test
	public void createAndGetSegmentResultTest()
	{
		//given
		final String sessionId = sessionService.getCurrentSession().getSessionId();
		final BTGSegmentModel segment = createAndSaveSegment(BTGResultScope.SESSION);
		final BTGSegmentResultModel createdSegmentResultModel = resultDataCachingBTGDao.createSegmentResult(customerA, segment,
				BTGResultScope.SESSION, false, sessionId, false);

		//when
		final BTGSegmentResultModel segmentResultModel = resultDataCachingBTGDao.getLastResult(customerA, segment,
				BTGResultScope.SESSION, sessionId);

		//then
		assertSegmentResultEquals(createdSegmentResultModel, segmentResultModel, true, true, true);
	}

	@Test
	public void createAndGetRuleResultTest()
	{
		//given
		final String sessionId = sessionService.getCurrentSession().getSessionId();
		final BTGRuleModel rule = createAndSaveRule(BTGResultScope.PERMANENT);
		final BTGRuleResultModel createdRuleResultModel = resultDataCachingBTGDao.createRuleResult(customerA, rule,
				BTGResultScope.PERMANENT, false, sessionId, false);

		//when
		final BTGRuleResultModel ruleResultModel = resultDataCachingBTGDao.getLastResult(customerA, rule, BTGResultScope.PERMANENT,
				sessionId);

		//then
		assertRuleResultEquals(createdRuleResultModel, ruleResultModel, true, true, true);
	}

	@Test
	public void createAndGetConditionResultTest()
	{
		//given
		final String sessionId = sessionService.getCurrentSession().getSessionId();
		final BTGExpressionModel condition = (BTGExpressionModel) createAndSaveCondition(BTGResultScope.PERMANENT);
		final BTGConditionResultModel createdConditionResultModel = resultDataCachingBTGDao.createConditionResult(customerA,
				condition, BTGResultScope.PERMANENT, false, sessionId, false);

		//when
		final BTGConditionResultModel conditionResultModel = resultDataCachingBTGDao.getLastResult(customerA, condition,
				BTGResultScope.PERMANENT, sessionId);

		//then
		assertConditionResultEquals(createdConditionResultModel, conditionResultModel, true, true, true);
	}

	@Test
	public void sessionResultShouldNotBeStoredInDatabaseTest() throws Exception
	{
		//given
		final String sessionId = sessionService.getCurrentSession().getSessionId();
		final BTGSegmentModel segment = createAndSaveSegment(BTGResultScope.SESSION);
		userService.setCurrentUser(customerA);

		//when
		final BTGSegmentResultModel segmentResultModel = resultDataCachingBTGDao.createSegmentResult(customerA, segment,
				BTGResultScope.SESSION, false, sessionId, false);

		//then
		assertTrue(segmentResultModel.getPk() == null);
		assertThereIsNoResultInDatabase(sessionId);
	}

	@Test
	public void permanentScopeResultsShouldBeStoredInDatabaseTest() throws Exception
	{
		//given
		final String sessionId = sessionService.getCurrentSession().getSessionId();
		final BTGSegmentModel segment = createAndSaveSegment(BTGResultScope.PERMANENT);
		userService.setCurrentUser(customerA);

		//when
		final BTGSegmentResultModel segmentResultModel = resultDataCachingBTGDao.createSegmentResult(customerA, segment,
				BTGResultScope.PERMANENT, false, sessionId, false);

		//then
		//Permament scope result should be stored in database
		assertTrue(segmentResultModel.getPk() != null);
		assertResultExistsInDatabase(sessionId, 1);
	}

	@Test
	public void permanentResultRemovedFromDatabaseTest() throws Exception
	{
		//given
		final String sessionId = sessionService.getCurrentSession().getSessionId();
		final BTGSegmentModel segment = createAndSaveSegment(BTGResultScope.PERMANENT);
		userService.setCurrentUser(customerA);
		final BTGSegmentResultModel segmentResultModel = resultDataCachingBTGDao.createSegmentResult(customerA, segment,
				BTGResultScope.PERMANENT, false, sessionId, false);
		assertTrue(segmentResultModel.getPk() != null);
		assertResultExistsInDatabase(sessionId, 1);

		//when
		modelService.remove(segmentResultModel);
		final BTGSegmentResultModel foundResult = resultDataCachingBTGDao.getLastResult(customerA, segment,
				BTGResultScope.PERMANENT, sessionId);

		//then
		assertNull(foundResult);
		assertThereIsNoResultInDatabase(sessionId);
	}

	@Test
	public void isSegmentResultChangedTest()
	{
		//given
		final boolean oldResult = true;
		final boolean newResult = true;
		final String sessionId = sessionService.getCurrentSession().getSessionId();
		final BTGSegmentModel segment = createAndSaveSegment(BTGResultScope.PERMANENT);
		resultDataCachingBTGDao.createSegmentResult(customerA, segment, BTGResultScope.PERMANENT, oldResult, sessionId, false);

		//when
		final boolean hasChanged = resultDataCachingBTGDao.isSegmentResultChanged(customerA, segment, BTGResultScope.PERMANENT,
				newResult, sessionId);

		//then
		assertFalse(hasChanged);
	}

	@Test
	public void isRuleResultChangedTest()
	{
		//given
		final boolean oldResult = true;
		final boolean newResult = false;
		final String sessionId = sessionService.getCurrentSession().getSessionId();
		final BTGRuleModel rule = createAndSaveRule(BTGResultScope.SESSION);
		resultDataCachingBTGDao.createRuleResult(customerA, rule, BTGResultScope.SESSION, oldResult, sessionId, false);

		//when
		final boolean hasChanged = resultDataCachingBTGDao.isRuleResultChanged(customerA, rule, BTGResultScope.SESSION, newResult,
				sessionId);

		//then
		assertTrue(hasChanged);
	}

	@Test
	public void isConditionResultChangedTest()
	{
		//given
		final boolean oldResult = false;
		final boolean newResult = true;
		final String sessionId = sessionService.getCurrentSession().getSessionId();
		final BTGExpressionModel condition = (BTGExpressionModel) createAndSaveCondition(BTGResultScope.PERMANENT);
		resultDataCachingBTGDao.createConditionResult(customerA, condition, BTGResultScope.PERMANENT, oldResult, sessionId, false);

		//when
		final boolean hasChanged = resultDataCachingBTGDao.isConditionResultChanged(customerA, condition, BTGResultScope.PERMANENT,
				newResult, sessionId);

		//then
		assertTrue(hasChanged);
	}

	@Test
	public void saveSegmentResultTest()
	{
		//given
		final boolean oldForced = false;
		final boolean newForced = true;
		final String sessionId = sessionService.getCurrentSession().getSessionId();
		final BTGSegmentModel segment = createAndSaveSegment(BTGResultScope.SESSION);
		resultDataCachingBTGDao.createSegmentResult(customerA, segment, BTGResultScope.SESSION, false, sessionId, oldForced);
		BTGSegmentResultModel segmentResultModel = resultDataCachingBTGDao.getLastResult(customerA, segment,
				BTGResultScope.SESSION, sessionId);

		//when
		segmentResultModel.setForced(newForced);
		resultDataCachingBTGDao.saveSegmentResult(segmentResultModel);

		//then
		segmentResultModel = resultDataCachingBTGDao.getLastResult(customerA, segment, BTGResultScope.SESSION, sessionId);
		assertTrue(segmentResultModel.isForced() == newForced);
	}

	@Test
	public void saveRuleResultTest()
	{
		//given
		final boolean oldForced = true;
		final boolean newForced = false;
		final String sessionId = sessionService.getCurrentSession().getSessionId();
		final BTGRuleModel rule = createAndSaveRule(BTGResultScope.SESSION);
		resultDataCachingBTGDao.createRuleResult(customerA, rule, BTGResultScope.SESSION, false, sessionId, oldForced);
		BTGRuleResultModel ruleResultModel = resultDataCachingBTGDao.getLastResult(customerA, rule, BTGResultScope.SESSION,
				sessionId);

		//when
		ruleResultModel.setForced(newForced);
		resultDataCachingBTGDao.saveRuleResult(ruleResultModel);

		//then
		ruleResultModel = resultDataCachingBTGDao.getLastResult(customerA, rule, BTGResultScope.SESSION, sessionId);
		assertTrue(ruleResultModel.isForced() == newForced);
	}

	@Test
	public void saveConditionResultTest()
	{
		//given
		final boolean oldForced = false;
		final boolean newForced = true;
		final String sessionId = sessionService.getCurrentSession().getSessionId();
		final BTGExpressionModel condition = (BTGExpressionModel) createAndSaveCondition(BTGResultScope.PERMANENT);
		resultDataCachingBTGDao.createConditionResult(customerA, condition, BTGResultScope.PERMANENT, false, sessionId, oldForced);
		BTGConditionResultModel conditionResultModel = resultDataCachingBTGDao.getLastResult(customerA, condition,
				BTGResultScope.PERMANENT, sessionId);

		//when
		conditionResultModel.setForced(newForced);
		resultDataCachingBTGDao.saveConditionResult(conditionResultModel);

		//then
		conditionResultModel = resultDataCachingBTGDao.getLastResult(customerA, condition, BTGResultScope.PERMANENT, sessionId);
		assertTrue(conditionResultModel.isForced() == newForced);
	}

	@Test
	public void saveNotCreatedResultTest()
	{
		//given
		final String sessionId = sessionService.getCurrentSession().getSessionId();
		final BTGSegmentModel segment = createAndSaveSegment(BTGResultScope.SESSION);
		final BTGSegmentResultModel newSegmentResultModel = createSegmentResultModel(customerA, segment, BTGResultScope.SESSION,
				false, sessionId, false, false);

		//when
		resultDataCachingBTGDao.saveSegmentResult(newSegmentResultModel);

		//then
		final BTGSegmentResultModel segmentResultModel = resultDataCachingBTGDao.getLastResult(customerA, segment,
				BTGResultScope.SESSION, sessionId);
		assertSegmentResultEquals(newSegmentResultModel, segmentResultModel, true, true, true);
	}

	@Test
	public void savePermanentScopeResultTest()
	{
		//given
		final String sessionId = sessionService.getCurrentSession().getSessionId();
		final BTGSegmentModel segment = createAndSaveSegment(BTGResultScope.PERMANENT);
		final BTGSegmentResultModel newSegmentResultModel = createSegmentResultModel(customerA, segment, BTGResultScope.PERMANENT,
				false, sessionId, false, false);

		//when
		resultDataCachingBTGDao.saveSegmentResult(newSegmentResultModel);

		//then
		final BTGSegmentResultModel segmentResultModel = resultDataCachingBTGDao.getLastResult(customerA, segment,
				BTGResultScope.PERMANENT, sessionId);
		assertSegmentResultEquals(newSegmentResultModel, segmentResultModel, true, true, true);
		assertResultExistsInDatabase(sessionId, 1);
	}

	@Test
	public void moveSessionResultTest()
	{
		//given
		final String sessionId = sessionService.getCurrentSession().getSessionId();
		final BTGEvaluationContext context = new BTGEvaluationContext(BTGConditionEvaluationScope.ONLINE,
				BTGEvaluationMethod.OPTIMIZED, BTGResultScope.SESSION);
		final BTGSegmentModel segment = createAndSaveSegment(BTGResultScope.SESSION);
		final BTGSegmentResultModel createdSegmentResultModel = resultDataCachingBTGDao.createSegmentResult(anonymous, segment,
				BTGResultScope.SESSION, false, sessionId, false);
		final BTGRuleModel rule = segment.getRules().iterator().next();
		final BTGRuleResultModel createdRuleResultModel = resultDataCachingBTGDao.createRuleResult(anonymous, rule,
				BTGResultScope.SESSION, false, sessionId, false);
		final BTGExpressionModel condition = (BTGExpressionModel) rule.getConditions().iterator().next();
		final BTGConditionResultModel createdConditionResultModel = resultDataCachingBTGDao.createConditionResult(anonymous,
				condition, BTGResultScope.SESSION, false, sessionId, false);

		//when
		resultDataCachingBTGDao.moveSessionResults(anonymous, sessionId, customerA, context);

		//then
		assertNull("There should be no segment result for anonymous user",
				resultDataCachingBTGDao.getLastResult(anonymous, segment, BTGResultScope.SESSION, sessionId));
		assertNull("There should be no rule result for anonymous user",
				resultDataCachingBTGDao.getLastResult(anonymous, rule, BTGResultScope.SESSION, sessionId));
		assertNull("There should be no condition result for anonymous user",
				resultDataCachingBTGDao.getLastResult(anonymous, condition, BTGResultScope.SESSION, sessionId));
		assertSegmentResultEquals(createdSegmentResultModel,
				resultDataCachingBTGDao.getLastResult(customerA, segment, BTGResultScope.SESSION, sessionId), true, false, true);
		assertRuleResultEquals(createdRuleResultModel,
				resultDataCachingBTGDao.getLastResult(customerA, rule, BTGResultScope.SESSION, sessionId), true, false, true);
		assertConditionResultEquals(createdConditionResultModel,
				resultDataCachingBTGDao.getLastResult(customerA, condition, BTGResultScope.SESSION, sessionId), true, false, true);
	}

	@Test
	public void moveSessionResultForPermanentScopeTest()
	{
		//given
		final String sessionId = sessionService.getCurrentSession().getSessionId();
		final BTGEvaluationContext context = new BTGEvaluationContext(BTGConditionEvaluationScope.ONLINE,
				BTGEvaluationMethod.OPTIMIZED, BTGResultScope.PERMANENT);
		final BTGSegmentModel segment = createAndSaveSegment(BTGResultScope.SESSION);
		final BTGSegmentResultModel createdSegmentResultModel = resultDataCachingBTGDao.createSegmentResult(anonymous, segment,
				BTGResultScope.SESSION, false, sessionId, false);
		final BTGRuleModel rule = segment.getRules().iterator().next();
		final BTGRuleResultModel createdRuleResultModel = resultDataCachingBTGDao.createRuleResult(anonymous, rule,
				BTGResultScope.SESSION, false, sessionId, false);
		final BTGExpressionModel condition = (BTGExpressionModel) rule.getConditions().iterator().next();
		final BTGConditionResultModel createdConditionResultModel = resultDataCachingBTGDao.createConditionResult(anonymous,
				condition, BTGResultScope.SESSION, false, sessionId, false);

		//when
		resultDataCachingBTGDao.moveSessionResults(anonymous, sessionId, customerA, context);

		//then
		assertNull("There should be no segment result for anonymous user",
				resultDataCachingBTGDao.getLastResult(anonymous, segment, BTGResultScope.SESSION, sessionId));
		assertNull("There should be no rule result for anonymous user",
				resultDataCachingBTGDao.getLastResult(anonymous, rule, BTGResultScope.SESSION, sessionId));
		assertNull("There should be no condition result for anonymous user",
				resultDataCachingBTGDao.getLastResult(anonymous, condition, BTGResultScope.SESSION, sessionId));

		assertNull("There should be no segment result for user in session scope",
				resultDataCachingBTGDao.getLastResult(customerA, segment, BTGResultScope.SESSION, sessionId));
		assertNull("There should be no rule result for user in session scope",
				resultDataCachingBTGDao.getLastResult(customerA, rule, BTGResultScope.SESSION, sessionId));
		assertNull("There should be no condition result for user in session scope",
				resultDataCachingBTGDao.getLastResult(customerA, condition, BTGResultScope.SESSION, sessionId));

		assertSegmentResultEquals(createdSegmentResultModel,
				resultDataCachingBTGDao.getLastResult(customerA, segment, BTGResultScope.PERMANENT, sessionId), false, false, false);
		assertRuleResultEquals(createdRuleResultModel,
				resultDataCachingBTGDao.getLastResult(customerA, rule, BTGResultScope.PERMANENT, sessionId), false, false, false);
		assertConditionResultEquals(createdConditionResultModel,
				resultDataCachingBTGDao.getLastResult(customerA, condition, BTGResultScope.PERMANENT, sessionId), false, false, false);
		assertResultExistsInDatabase(sessionId, 3);
	}

	@Test
	public void moveSessionResultForManySegmentsTest()
	{
		//given
		final String sessionId = sessionService.getCurrentSession().getSessionId();
		final BTGEvaluationContext context = new BTGEvaluationContext(BTGConditionEvaluationScope.ONLINE,
				BTGEvaluationMethod.OPTIMIZED, BTGResultScope.SESSION);
		createSegmentA();
		createSegmentB();
		final int segmentAResultCount = createResult(segmentA, anonymous, BTGResultScope.SESSION, sessionId);
		final int segmentBResultCount = createResult(segmentB, anonymous, BTGResultScope.SESSION, sessionId);
		final int resultCount = segmentAResultCount + segmentBResultCount;

		//when
		resultDataCachingBTGDao.moveSessionResults(anonymous, sessionId, customerA, context);

		//then
		assertTrue("There should be no resutl for anonymous user",
				0 == countExistingResult(segmentA, anonymous, BTGResultScope.SESSION, sessionId));
		assertTrue("There should be no resutl for anonymous user",
				0 == countExistingResult(segmentB, anonymous, BTGResultScope.SESSION, sessionId));

		assertTrue("There should be " + segmentAResultCount + " results for customerA and segmentA",
				segmentAResultCount == countExistingResult(segmentA, customerA, BTGResultScope.SESSION, sessionId));
		assertTrue("There should be " + segmentAResultCount + " results for customerA adn segmentB",
				segmentBResultCount == countExistingResult(segmentB, customerA, BTGResultScope.SESSION, sessionId));

		assertTrue("There should be " + resultCount + " results in cache", resultCount == resultDataCachingBTGDao.getResultMap()
				.size());
	}

	@Test
	public void moveSessionResultWhenSegmentNotExistAnymoreTest()
	{
		//given
		final String sessionId = sessionService.getCurrentSession().getSessionId();
		final BTGEvaluationContext context = new BTGEvaluationContext(BTGConditionEvaluationScope.ONLINE,
				BTGEvaluationMethod.OPTIMIZED, BTGResultScope.SESSION);
		final BTGSegmentModel segment = createAndSaveSegment(BTGResultScope.SESSION);
		resultDataCachingBTGDao.createSegmentResult(anonymous, segment, BTGResultScope.SESSION, false, sessionId, false);

		//when
		modelService.remove(segment);
		resultDataCachingBTGDao.moveSessionResults(anonymous, sessionId, customerA, context);

		//then
		assertNull("There should be no segment result for anonymous user",
				resultDataCachingBTGDao.getLastResult(anonymous, segment, BTGResultScope.SESSION, sessionId));
		assertNull("There should be no segment result for customerA",
				resultDataCachingBTGDao.getLastResult(customerA, segment, BTGResultScope.SESSION, sessionId));
	}

	@Test
	public void moveSessionResultWhenRuleNotExistAnymoreTest()
	{
		//given
		final String sessionId = sessionService.getCurrentSession().getSessionId();
		final BTGEvaluationContext context = new BTGEvaluationContext(BTGConditionEvaluationScope.ONLINE,
				BTGEvaluationMethod.OPTIMIZED, BTGResultScope.SESSION);
		final BTGSegmentModel segment = createAndSaveSegment(BTGResultScope.SESSION);
		final BTGRuleModel rule = segment.getRules().iterator().next();
		resultDataCachingBTGDao.createRuleResult(anonymous, rule, BTGResultScope.SESSION, false, sessionId, false);

		//when
		modelService.remove(rule);
		resultDataCachingBTGDao.moveSessionResults(anonymous, sessionId, customerA, context);

		//then
		assertNull("There should be no rule result for anonymous user",
				resultDataCachingBTGDao.getLastResult(anonymous, rule, BTGResultScope.SESSION, sessionId));
		assertNull("There should be no rule result for customerA",
				resultDataCachingBTGDao.getLastResult(customerA, rule, BTGResultScope.SESSION, sessionId));
	}

	@Test
	public void moveSessionResultWhenConditionNotExistAnymoreTest()
	{
		//given
		final String sessionId = sessionService.getCurrentSession().getSessionId();
		final BTGEvaluationContext context = new BTGEvaluationContext(BTGConditionEvaluationScope.ONLINE,
				BTGEvaluationMethod.OPTIMIZED, BTGResultScope.SESSION);
		final BTGSegmentModel segment = createAndSaveSegment(BTGResultScope.SESSION);
		final BTGRuleModel rule = segment.getRules().iterator().next();
		final BTGExpressionModel condition = (BTGExpressionModel) rule.getConditions().iterator().next();
		resultDataCachingBTGDao.createConditionResult(anonymous, condition, BTGResultScope.SESSION, false, sessionId, false);

		//when
		modelService.remove(condition);
		resultDataCachingBTGDao.moveSessionResults(anonymous, sessionId, customerA, context);

		//then0
		assertNull("There should be no condition result for anonymous user",
				resultDataCachingBTGDao.getLastResult(anonymous, condition, BTGResultScope.SESSION, sessionId));
		assertNull("There should be no condition result for customerA",
				resultDataCachingBTGDao.getLastResult(customerA, condition, BTGResultScope.SESSION, sessionId));
	}

	protected int createResult(final BTGSegmentModel segment, final UserModel user, final BTGResultScope scope,
			final String sessionId)
	{
		resultDataCachingBTGDao.createSegmentResult(user, segment, scope, false, sessionId, false);
		int resultCount = 1;
		for (final BTGRuleModel rule : segment.getRules())
		{
			resultDataCachingBTGDao.createRuleResult(user, rule, scope, false, sessionId, false);
			resultCount++;
			for (final BTGConditionModel condition : rule.getConditions())
			{
				resultDataCachingBTGDao.createConditionResult(user, (BTGExpressionModel) condition, scope, false, sessionId, false);
				resultCount++;
			}
		}
		return resultCount;
	}

	protected int countExistingResult(final BTGSegmentModel segment, final UserModel user, final BTGResultScope scope,
			final String sessionId)
	{
		int resultCount = 0;
		final BTGAbstractResultModel result = resultDataCachingBTGDao.getLastResult(user, segment, scope, sessionId);
		if (result != null)
		{
			resultCount++;
		}

		for (final BTGRuleModel rule : segment.getRules())
		{
			if (resultDataCachingBTGDao.getLastResult(user, rule, scope, sessionId) != null)
			{
				resultCount++;
			}
			for (final BTGConditionModel condition : rule.getConditions())
			{
				if (resultDataCachingBTGDao.getLastResult(user, condition, scope, sessionId) != null)
				{
					resultCount++;
				}
			}
		}
		return resultCount;
	}

	private boolean setInvalidated(final BTGAbstractResultModel resultModel)
	{
		final boolean invalidated = !resultDataCachingBTGDao.isInvalidatedWhenNoCacheEntryExists();
		resultDataCachingBTGDao.setInvalidated(resultModel, invalidated);
		return invalidated;
	}

	protected BTGSegmentResultModel createSegmentResultModel(final UserModel user, final BTGSegmentModel segment,
			final BTGResultScope scope, final boolean fulfilled, final String jaloSessionId, final boolean forced,
			final boolean invalidated)
	{
		final BTGSegmentResultModel segmentResult = new BTGSegmentResultModel();
		segmentResult.setUser(user);
		segmentResult.setSegment(segment);
		segmentResult.setFulfilled(fulfilled);
		segmentResult.setForced(forced);
		segmentResult.setResultScope(scope);
		segmentResult.setSessionId(jaloSessionId);
		segmentResult.setInvalidated(invalidated);
		return segmentResult;
	}

	protected BTGSegmentModel createAndSaveSegment(final BTGResultScope resultScope)
	{
		final BTGSegmentModel segment = createSegment();
		segment.setDefaultResultScope(resultScope);
		modelService.save(segment);
		final BTGRuleModel rule = createRule("testRule");
		segment.setRules(Collections.singletonList(rule));

		rule.setConditions(Collections.singletonList(createExpression(
				createOrderOperand(BTGOrderTotalSumOperandModel.class, 2, ""), createOperator(PriceExpressionEvaluator.EQUALS),
				createPriceReferenceOperand(5, EUR))));
		modelService.save(segment);
		return segment;
	}

	protected BTGRuleModel createAndSaveRule(final BTGResultScope resultScope)
	{
		final BTGSegmentModel segment = createSegment();
		segment.setDefaultResultScope(resultScope);
		modelService.save(segment);
		final BTGRuleModel rule = createRule("testRule");
		segment.setRules(Collections.singletonList(rule));

		rule.setConditions(Collections.singletonList(createExpression(
				createOrderOperand(BTGOrderTotalSumOperandModel.class, 2, ""), createOperator(PriceExpressionEvaluator.EQUALS),
				createPriceReferenceOperand(5, EUR))));
		modelService.save(segment);
		return rule;
	}

	protected BTGConditionModel createAndSaveCondition(final BTGResultScope resultScope)
	{
		final BTGSegmentModel segment = createSegment();
		segment.setDefaultResultScope(resultScope);
		modelService.save(segment);
		final BTGRuleModel rule = createRule("testRule");
		segment.setRules(Collections.singletonList(rule));

		final BTGConditionModel condition = createExpression(createOrderOperand(BTGOrderTotalSumOperandModel.class, 2, ""),
				createOperator(PriceExpressionEvaluator.EQUALS), createPriceReferenceOperand(5, EUR));
		rule.setConditions(Collections.singletonList(condition));
		modelService.save(segment);
		return condition;
	}

	protected void assertConditionResultEquals(final BTGConditionResultModel result1, final BTGConditionResultModel result2,
			final boolean pkShouldBeEqual, final boolean userShouldBeEqual, final boolean scopeShouldBeEqual)
	{
		assertAbstractResutlEquals(result1, result2, pkShouldBeEqual, scopeShouldBeEqual);
		assertEquals("Condition should be equal", result1.getCondition(), result2.getCondition());
		if (userShouldBeEqual)
		{
			assertEquals("User should be equal", result1.getUser(), result2.getUser());
		}
		else
		{
			assertNotEquals("User should not be equal", result1.getUser(), result2.getUser());
		}
	}

	protected void assertRuleResultEquals(final BTGRuleResultModel result1, final BTGRuleResultModel result2,
			final boolean pkShouldBeEqual, final boolean userShouldBeEqual, final boolean scopeShouldBeEqual)
	{
		assertAbstractResutlEquals(result1, result2, pkShouldBeEqual, scopeShouldBeEqual);
		assertEquals("Rule should be equal", result1.getRule(), result2.getRule());
		if (userShouldBeEqual)
		{
			assertEquals("User should be equal", result1.getUser(), result2.getUser());
		}
		else
		{
			assertNotEquals("User should not be equal", result1.getUser(), result2.getUser());
		}
	}

	protected void assertSegmentResultEquals(final BTGSegmentResultModel result1, final BTGSegmentResultModel result2,
			final boolean pkShouldBeEqual, final boolean userShouldBeEqual, final boolean scopeShouldBeEqual)
	{
		assertAbstractResutlEquals(result1, result2, pkShouldBeEqual, scopeShouldBeEqual);
		assertEquals("Segment should be equal", result1.getSegment(), result2.getSegment());
		if (userShouldBeEqual)
		{
			assertEquals("User should be equal", result1.getUser(), result2.getUser());
		}
		else
		{
			assertNotEquals("User should not be equal", result1.getUser(), result2.getUser());
		}
	}

	protected void assertAbstractResutlEquals(final BTGAbstractResultModel result1, final BTGAbstractResultModel result2,
			final boolean pkShouldBeEqual, final boolean scopeShouldBeEqual)
	{
		assertNotNull("Result should not be null", result1);
		assertNotNull("Result should not be null", result2);
		assertTrue("Forced field should be equal", result1.isForced() == result2.isForced());
		assertTrue("Fulfield field should be equal", result1.isFulfilled() == result2.isFulfilled());
		assertTrue("Invalidated field should be equal", result1.isInvalidated() == result2.isInvalidated());
		assertEquals("SessionId should be equal", result1.getSessionId(), result2.getSessionId());
		if (pkShouldBeEqual)
		{
			assertEquals("Pk should be equal", result1.getPk(), result2.getPk());
		}
		else
		{
			assertNotEquals("Pk should not be equal", result1.getPk(), result2.getPk());
		}

		if (scopeShouldBeEqual)
		{
			assertEquals("Result scope should be equal", result1.getResultScope(), result2.getResultScope());
		}
		else
		{
			assertNotEquals("Result scope should not be equal", result1.getResultScope(), result2.getResultScope());
		}
	}

	protected void assertResultExistsInDatabase(final String sessionId, final int resultCount)
	{
		final List<BTGAbstractResultModel> resultList = getBtgResultForSession(sessionId);
		assertEquals("There should be " + resultCount + " results in database", resultCount, resultList.size());
	}

	protected void assertThereIsNoResultInDatabase(final String sessionId)
	{
		final List<BTGAbstractResultModel> resultList = getBtgResultForSession(sessionId);
		assertEquals("There should be no result in database", 0, resultList.size());
	}

	protected List<BTGAbstractResultModel> getBtgResultForSession(final String sessionId)
	{
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(SELECT_RESULT_FOR_SESSION);
		fQuery.addQueryParameter("sessionId", sessionId);

		fQuery.setResultClassList(Collections.singletonList(BTGAbstractResultModel.class));
		final SearchResult<BTGAbstractResultModel> searchResult = flexibleSearchService.search(fQuery);
		return searchResult.getResult();
	}
}
