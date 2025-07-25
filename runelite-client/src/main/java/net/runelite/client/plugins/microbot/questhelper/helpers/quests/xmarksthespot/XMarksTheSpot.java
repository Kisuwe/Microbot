/*
 * Copyright (c) 2019, Trevor <https://github.com/Trevor159>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.microbot.questhelper.helpers.quests.xmarksthespot;

import net.runelite.client.plugins.microbot.questhelper.collections.ItemCollections;
import net.runelite.client.plugins.microbot.questhelper.panel.PanelDetails;
import net.runelite.client.plugins.microbot.questhelper.questhelpers.BasicQuestHelper;
import net.runelite.client.plugins.microbot.questhelper.requirements.item.ItemRequirement;
import net.runelite.client.plugins.microbot.questhelper.rewards.ItemReward;
import net.runelite.client.plugins.microbot.questhelper.rewards.QuestPointReward;
import net.runelite.client.plugins.microbot.questhelper.steps.DigStep;
import net.runelite.client.plugins.microbot.questhelper.steps.NpcStep;
import net.runelite.client.plugins.microbot.questhelper.steps.QuestStep;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.gameval.NpcID;

import java.util.*;

public class XMarksTheSpot extends BasicQuestHelper
{
	//Items Required
	ItemRequirement spade;

	// Items recommended
	ItemRequirement glory;

	QuestStep speakVeosLumbridge, digOutsideBob, digCastle, digDraynor, digMartin, speakVeosSarim, speakVeosSarimWithoutCasket;

	@Override
	public Map<Integer, QuestStep> loadSteps()
	{
		initializeRequirements();
		setupSteps();

		Map<Integer, QuestStep> steps = new HashMap<>();

		steps.put(0, speakVeosLumbridge);
		steps.put(1, steps.get(0));
		steps.put(2, digOutsideBob);
		steps.put(3, digCastle);
		steps.put(4, digDraynor);
		steps.put(5, digMartin);
		steps.put(6, speakVeosSarim);
		steps.put(7, speakVeosSarimWithoutCasket);

		return steps;
	}

	@Override
	protected void setupRequirements()
	{
		spade = new ItemRequirement("Spade", ItemID.SPADE).isNotConsumed();
		glory = new ItemRequirement("Amulet of Glory for faster teleport to Draynor Village.", ItemCollections.AMULET_OF_GLORIES).isNotConsumed();
	}

	private void setupSteps()
	{
		// TODO: Worth adding PuzzleWrapperStep at all given the Clue Plugin also does this?
		speakVeosLumbridge = new NpcStep(this, NpcID.VEOS_VISIBLE, new WorldPoint(3228, 3242, 0),
			"Talk to Veos in The Sheared Ram pub in Lumbridge to start the quest.");
		speakVeosLumbridge.addDialogStep("I'm looking for a quest.");
		speakVeosLumbridge.addDialogStep("Sounds good, what should I do?");
		speakVeosLumbridge.addDialogSteps("Can I help?", "Yes.");

		digOutsideBob = new DigStep(this, new WorldPoint(3230, 3209, 0),
			"Dig north of Bob's Brilliant Axes, on the west side of the plant against the wall of his house.");
		digOutsideBob.addDialogStep("Okay, thanks Veos.");

		digCastle = new DigStep(this, new WorldPoint(3203, 3212, 0),
			"Dig behind Lumbridge Castle, just outside the kitchen door.");

		digDraynor = new DigStep(this, new WorldPoint(3109, 3264, 0),
			"Dig north-west of the Draynor Village jail, just by the wheat farm.");

		digMartin = new DigStep(this, new WorldPoint(3078, 3259, 0),
			"Dig in the pig pen just west where Martin the Master Gardener is.",
			new ItemRequirement("Treasure scroll", ItemID.CLUEQUEST_CLUE4));

		ItemRequirement ancientCasket = new ItemRequirement("Ancient casket", ItemID.CLUEQUEST_CASKET);
		ancientCasket.setTooltip("If you've lost this you can get another by digging in the pig pen in Draynor Village.");

		speakVeosSarim = new NpcStep(this, NpcID.VEOS_VISIBLE, new WorldPoint(3054, 3245, 0),
			"Talk to Veos directly south of the Rusty Anchor Inn in Port Sarim to finish the quest.",
			ancientCasket);
		((NpcStep) speakVeosSarim).addAlternateNpcs(NpcID.VEOS_VISIBLE_TRAVEL);

		speakVeosSarimWithoutCasket = new NpcStep(this, NpcID.VEOS_VISIBLE, new WorldPoint(3054, 3245, 0),
			"Talk to Veos directly south of the Rusty Anchor Inn in Port Sarim to finish the quest.");
		((NpcStep) speakVeosSarimWithoutCasket).addAlternateNpcs(NpcID.VEOS_VISIBLE_TRAVEL);

		speakVeosSarim.addSubSteps(speakVeosSarimWithoutCasket);
	}

	@Override
	public List<ItemRequirement> getItemRequirements()
	{
		ArrayList<ItemRequirement> reqs = new ArrayList<>();
		reqs.add(spade);
		return reqs;
	}

	@Override
	public List<ItemRequirement> getItemRecommended()
	{
		ArrayList<ItemRequirement> reqs = new ArrayList<>();
		reqs.add(glory);
		return reqs;
	}

	@Override
	public QuestPointReward getQuestPointReward()
	{
		return new QuestPointReward(1);
	}

	@Override
	public List<ItemReward> getItemRewards()
	{
		return Arrays.asList(
			new ItemReward("300 Exp. Lamp (Any Skill)", ItemID.THOSF_REWARD_LAMP, 1),
			new ItemReward("Coins", ItemID.COINS, 200),
			new ItemReward("A Beginner Clue Scroll", ItemID.TRAIL_CLUE_BEGINNER, 1));
	}

	@Override
	public List<PanelDetails> getPanels()
	{
		List<PanelDetails> allSteps = new ArrayList<>();
		allSteps.add(new PanelDetails("Speak to Veos", Collections.singletonList(speakVeosLumbridge), spade));
		allSteps.add(new PanelDetails("Solve the clue scroll", Arrays.asList(digOutsideBob, digCastle, digDraynor, digMartin)));
		allSteps.add(new PanelDetails("Bring the casket to Veos", Collections.singletonList(speakVeosSarim)));
		return allSteps;
	}
}
