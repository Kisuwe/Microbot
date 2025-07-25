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
package net.runelite.client.plugins.microbot.questhelper.helpers.quests.impcatcher;

import net.runelite.client.plugins.microbot.questhelper.panel.PanelDetails;
import net.runelite.client.plugins.microbot.questhelper.questhelpers.BasicQuestHelper;
import net.runelite.client.plugins.microbot.questhelper.requirements.conditional.Conditions;
import net.runelite.client.plugins.microbot.questhelper.requirements.item.ItemRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.zone.Zone;
import net.runelite.client.plugins.microbot.questhelper.requirements.zone.ZoneRequirement;
import net.runelite.client.plugins.microbot.questhelper.rewards.ExperienceReward;
import net.runelite.client.plugins.microbot.questhelper.rewards.ItemReward;
import net.runelite.client.plugins.microbot.questhelper.rewards.QuestPointReward;
import net.runelite.client.plugins.microbot.questhelper.steps.*;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.gameval.NpcID;
import net.runelite.api.gameval.ObjectID;

import java.util.*;

public class ImpCatcher extends BasicQuestHelper
{
	//Items Required
	ItemRequirement blackBead, whiteBead, redBead, yellowBead;

	QuestStep moveToTower, climbTower, turnInQuest, collectBeads;

	Zone towerSecond, towerThird;

	ZoneRequirement inTowerSecond, inTowerThird;

	@Override
	public Map<Integer, QuestStep> loadSteps()
	{
		initializeRequirements();
		setupSteps();

		Map<Integer, QuestStep> steps = new HashMap<>();

		ConditionalStep doQuest = new ConditionalStep(this, collectBeads);
		doQuest.addStep(new Conditions(blackBead,whiteBead,redBead,yellowBead, inTowerThird), turnInQuest);
		doQuest.addStep(new Conditions(blackBead,whiteBead,redBead,yellowBead, inTowerSecond), climbTower);
		doQuest.addStep(new Conditions(blackBead,whiteBead,redBead,yellowBead), moveToTower);

		steps.put(0, doQuest);

		steps.put(1, doQuest);

		return steps;
	}



	@Override
	protected void setupRequirements() {
		blackBead = new ItemRequirement("Black bead", ItemID.BLACK_BEAD);
		whiteBead = new ItemRequirement("White bead", ItemID.WHITE_BEAD);
		redBead = new ItemRequirement("Red bead", ItemID.RED_BEAD);
		yellowBead = new ItemRequirement("Yellow bead", ItemID.YELLOW_BEAD);

		inTowerSecond = new ZoneRequirement(towerSecond);
		inTowerThird = new ZoneRequirement(towerThird);
	}

	public void setupSteps(){
		collectBeads = new DetailedQuestStep(this, "Collect one of each bead. You can kill imps for these beads, or buy them on the Grand Exchange.",
			blackBead, whiteBead, redBead, yellowBead);
		moveToTower = new ObjectStep(this, ObjectID.FAI_WIZTOWER_SPIRALSTAIRS, new WorldPoint(3103, 3159, 0),
			"Head to the Wizards' Tower and climb up the staircase with the required beads.", blackBead, whiteBead, redBead, yellowBead);
		climbTower = new ObjectStep(this, ObjectID.FAI_WIZTOWER_SPIRALSTAIRS_MIDDLE, new WorldPoint(3103, 3159, 1),
			"Climb the staircase again.", blackBead, whiteBead, redBead, yellowBead);
		turnInQuest = new NpcStep(this, NpcID.WIZARD_MIZGOG_QUEST, new WorldPoint(3103, 3163, 2),
			"Talk to Wizard Mizgog with the required beads to finish the quest.",
			blackBead, whiteBead, redBead, yellowBead);
		turnInQuest.addDialogSteps("Give me a quest please.", "Yes.");
	}

	@Override
	protected void setupZones(){
		towerSecond = new Zone(new WorldPoint(3089, 3176, 1), new WorldPoint(3126, 3146, 1));
		towerThird = new Zone(new WorldPoint(3089, 3176, 2), new WorldPoint(3126, 3146, 2));
	}

	@Override
	public List<ItemRequirement> getItemRequirements()
	{
		ArrayList<ItemRequirement> reqs = new ArrayList<>();
		reqs.add(blackBead);
		reqs.add(whiteBead);
		reqs.add(redBead);
		reqs.add(yellowBead);

		return reqs;
	}

	@Override
	public List<PanelDetails> getPanels()
	{
		List<PanelDetails> allSteps = new ArrayList<>();
		allSteps.add(new PanelDetails("Bring Mizgog his beads", Arrays.asList(collectBeads, moveToTower, climbTower, turnInQuest),
			blackBead, whiteBead, redBead, yellowBead));
		return allSteps;
	}

	@Override
	public QuestPointReward getQuestPointReward()
	{
		return new QuestPointReward(1);
	}

	@Override
	public List<ExperienceReward> getExperienceRewards()
	{
		return Collections.singletonList(new ExperienceReward(Skill.MAGIC, 875));
	}

	@Override
	public List<ItemReward> getItemRewards()
	{
		return Collections.singletonList(new ItemReward("An Amulet of Accuracy", ItemID.AMULET_OF_ACCURACY, 1));
	}

	@Override
	public List<String> getCombatRequirements()
	{
		return Collections.singletonList("Imps (level 2) if you plan on collecting the beads yourself");
	}
}
