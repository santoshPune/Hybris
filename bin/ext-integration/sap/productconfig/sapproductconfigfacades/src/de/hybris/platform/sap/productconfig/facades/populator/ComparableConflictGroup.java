/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.sap.productconfig.facades.populator;

import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Allows to sort conflict groups
 */
public class ComparableConflictGroup extends UiGroupData implements Comparable<ComparableConflictGroup>
{
	private List<UiGroupData> csticGroupsFlat;

	// We only need to compile the rank once and use it for every compare operation
	private Integer storedRank = null;

	/**
	 * @return the storedRank
	 */
	public Integer getStoredRank()
	{
		return storedRank;
	}


	/**
	 * @return the csticGroupsFlat
	 */
	public List<UiGroupData> getCsticGroupsFlat()
	{
		return csticGroupsFlat;
	}


	/**
	 * @param csticGroupsFlat
	 *           the csticGroupsFlat to set
	 */
	public void setCsticGroupsFlat(final List<UiGroupData> csticGroupsFlat)
	{
		this.csticGroupsFlat = csticGroupsFlat;
	}

	/**
	 * @return The rank of a conflict group. This is compiled from the list of cstic groups. The rank equals the number
	 *         of the first cstic which is part of the conflict
	 */
	public Integer rank()
	{
		if (storedRank != null)
		{
			return storedRank;
		}

		if (csticGroupsFlat == null)
		{
			throw new IllegalArgumentException("No list of groups containing all cstics");
		}

		final List<CsticData> csticsFromConflict = getCstics();
		if (csticsFromConflict == null)
		{
			throw new IllegalArgumentException("No cstics at conflict group");
		}

		final Set<String> myCstics = csticsFromConflict//
				.stream()//
				.map(a -> a.getName())//
				.collect(Collectors.toSet());


		return compileRankFromCsticList(myCstics);

	}


	protected Integer compileRankFromCsticList(final Set<String> myCstics)
	{
		int rank = 0;
		//now just check for the first occurrence in the list of flat cstic groups
		for (final UiGroupData uiGroup : csticGroupsFlat)
		{
			for (final CsticData cstic : uiGroup.getCstics())
			{
				rank++;
				if (myCstics.contains(cstic.getName()))
				{
					final Integer determinedRank = Integer.valueOf(rank);
					storedRank = determinedRank;
					return determinedRank;
				}
			}
		}
		throw new IllegalStateException("We didn't find the conflicting cstics within the list of all cstic groups");
	}

	@Override
	public int compareTo(final ComparableConflictGroup otherConflictGroup)
	{
		return rank().compareTo(otherConflictGroup.rank());
	}

	@Override
	public boolean equals(final Object another)
	{
		if (another instanceof ComparableConflictGroup)
		{
			return compareTo((ComparableConflictGroup) another) == 0;
		}
		else
		{
			return super.equals(another);
		}
	}

	@Override
	public int hashCode()
	{
		return rank().intValue();
	}

}
