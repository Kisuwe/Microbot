/*
 * Copyright (c) 2022, Zoinkwiz (https://github.com/Zoinkwiz)
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
package net.runelite.client.plugins.microbot.questhelper.helpers.quests.thegardenofdeath;

import net.runelite.client.plugins.microbot.questhelper.collections.ItemCollections;
import net.runelite.client.plugins.microbot.questhelper.panel.PanelDetails;
import net.runelite.client.plugins.microbot.questhelper.questhelpers.BasicQuestHelper;
import net.runelite.client.plugins.microbot.questhelper.questinfo.QuestVarbits;
import net.runelite.client.plugins.microbot.questhelper.requirements.Requirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.conditional.Conditions;
import net.runelite.client.plugins.microbot.questhelper.requirements.item.ItemRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.player.SkillRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.util.LogicType;
import net.runelite.client.plugins.microbot.questhelper.requirements.util.Operation;
import net.runelite.client.plugins.microbot.questhelper.requirements.var.VarbitRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.widget.WidgetTextRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.zone.Zone;
import net.runelite.client.plugins.microbot.questhelper.requirements.zone.ZoneRequirement;
import net.runelite.client.plugins.microbot.questhelper.rewards.ExperienceReward;
import net.runelite.client.plugins.microbot.questhelper.rewards.QuestPointReward;
import net.runelite.client.plugins.microbot.questhelper.steps.*;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.gameval.ObjectID;

import java.util.*;

public class TheGardenOfDeath extends BasicQuestHelper
{
	// Required
	ItemRequirement secateurs;

	// Recommended
	ItemRequirement antipoison, kharedstMemoir, fairyRingAccess, xericsTalisman;

	// Quest items
	ItemRequirement stoneTablet1, stoneTablet2, stoneTablet3, stoneTablet4, kasondesJournal, wordTranslations, dirtyNote1, dirtyNote2, dirtyNote3, compass, warningNote;

	QuestStep getJournal, readJournal, getSecateurs, enterHole, searchForTablet, readTablet1, readTranslations, attemptTranslation, inputWords1;

	QuestStep leaveHole, goToMolch, enterMolchHole, searchVines, cutVines, searchForTablet2, readTablet2, readTranslations2, attemptTranslation2, inputWords2;

	QuestStep leaveHole2, leaveMolchIsland, enterXericShrineHole, searchForTablet3, readTablet3, readTranslations3, attemptTranslation3, inputWords3;

	QuestStep leaveHole3, enterMorraHole, searchForTablet4, readTablet4, readTranslations4, attemptTranslation4, inputWords4;

	DetailedQuestStep readTabletFinal, readWarningNote;

	Zone hole, molchIsland, molchHole, xericHole, morraHole;

	Requirement translationOpen, chatInputOpen, hasCutVines, searchedVines, hasReadTablet2, inXericHole, hasReadTablet3, inMorraHole, hasReadTablet4;

	Requirement inHole, onMolchIsland, inMolchHole;

	@Override
	public Map<Integer, QuestStep> loadSteps()
	{
		initializeRequirements();
		setupConditions();
		setupSteps();
		Map<Integer, QuestStep> steps = new HashMap<>();

		steps.put(0, getJournal);
		steps.put(2, readJournal);

		ConditionalStep t1 = new ConditionalStep(this, getSecateurs);
		t1.addStep(chatInputOpen, inputWords1);
		t1.addStep(translationOpen, attemptTranslation);
		t1.addStep(wordTranslations, readTranslations);
		t1.addStep(stoneTablet1, readTablet1);
		t1.addStep(new Conditions(inHole, secateurs), searchForTablet);
		t1.addStep(secateurs, enterHole);

		steps.put(4, t1);
		// In hole, 3575 vplayer 0->256
		steps.put(6, t1);
		steps.put(8, t1);
		steps.put(10, t1);
		steps.put(12, t1);
		steps.put(14, t1);

		ConditionalStep t2 = new ConditionalStep(this, goToMolch);
		t2.addStep(new Conditions(hasReadTablet2, chatInputOpen), inputWords2);
		t2.addStep(new Conditions(hasReadTablet2, translationOpen), attemptTranslation2);
		t2.addStep(hasReadTablet2, readTranslations2);
		t2.addStep(stoneTablet2, readTablet2);
		t2.addStep(inHole, leaveHole);
		t2.addStep(new Conditions(inMolchHole, hasCutVines), searchForTablet2);
		t2.addStep(new Conditions(inMolchHole, searchedVines), cutVines);
		t2.addStep(inMolchHole, searchVines);
		t2.addStep(onMolchIsland, enterMolchHole);

		steps.put(16, t2);
		steps.put(18, t2);
		steps.put(20, t2);
		steps.put(22, t2);
		steps.put(24, t2);
		steps.put(26, t2);
		steps.put(28, t2);

		ConditionalStep t3 = new ConditionalStep(this, enterXericShrineHole);
		t3.addStep(new Conditions(hasReadTablet3, chatInputOpen), inputWords3);
		t3.addStep(new Conditions(hasReadTablet3, translationOpen), attemptTranslation3);
		t3.addStep(hasReadTablet3, readTranslations3);
		t3.addStep(stoneTablet3, readTablet3);
		t3.addStep(inXericHole, searchForTablet3);
		t3.addStep(onMolchIsland, leaveMolchIsland);
		t3.addStep(inMolchHole, leaveHole2);

		steps.put(30, t3);
		steps.put(32, t3);
		steps.put(34, t3);
		steps.put(36, t3);
		steps.put(38, t3);

		ConditionalStep t4 = new ConditionalStep(this, enterMorraHole);
		t4.addStep(new Conditions(hasReadTablet4, chatInputOpen), inputWords4);
		t4.addStep(new Conditions(hasReadTablet4, translationOpen), attemptTranslation4);
		t4.addStep(hasReadTablet4, readTranslations4);
		t4.addStep(stoneTablet4, readTablet4);
		t4.addStep(inMorraHole, searchForTablet4);
		t4.addStep(inXericHole, leaveHole3);

		steps.put(40, t4);
		steps.put(42, t4);
		steps.put(44, t4);
		steps.put(46, t4);
		steps.put(48, t4);

		ConditionalStep finalStep = new ConditionalStep(this, enterMorraHole);
		finalStep.addStep(warningNote.alsoCheckBank(questBank), readWarningNote);
		finalStep.addStep(stoneTablet4.alsoCheckBank(questBank), readTabletFinal);
		finalStep.addStep(inMorraHole, searchForTablet4);
		steps.put(50, finalStep);
		steps.put(52, finalStep);
		steps.put(54, finalStep);

		return steps;
	}

	@Override
	protected void setupRequirements()
	{
		// Required
		secateurs = new ItemRequirement("Secateurs (Obtainable in quest)", ItemID.SECATEURS);
		secateurs.canBeObtainedDuringQuest();
		secateurs.addAlternates(ItemID.FAIRY_ENCHANTED_SECATEURS);

		// Recommended
		antipoison = new ItemRequirement("Antipoisons", ItemCollections.ANTIPOISONS);
		kharedstMemoir = new ItemRequirement("Kharedst's memoirs or book of the dead for teleports", ItemID.VEOS_KHAREDSTS_MEMOIRS);
		kharedstMemoir.addAlternates(ItemID.BOOK_OF_THE_DEAD);
		fairyRingAccess = new ItemRequirement("Fairy ring access for quick access to Molch Island (DJR)", ItemCollections.FAIRY_STAFF);
		xericsTalisman = new ItemRequirement("Xeric's talisman", ItemID.XERIC_TALISMAN);

		// Quest items
		stoneTablet1 = new ItemRequirement("Stone tablet", ItemID.TGOD_TABLET_1);
		stoneTablet2 = new ItemRequirement("Stone tablet", ItemID.TGOD_TABLET_2);
		stoneTablet3 = new ItemRequirement("Stone tablet", ItemID.TGOD_TABLET_3);
		stoneTablet4 = new ItemRequirement("Stone tablet", ItemID.TGOD_TABLET_4);
		kasondesJournal = new ItemRequirement("Kasonde's journal", ItemID.TGOD_JOURNAL);
		wordTranslations = new ItemRequirement("Word translations", ItemID.TGOD_TRANSLATIONS);
		wordTranslations.setTooltip("Obtained in quest in a stone table in the first hole");
		dirtyNote1 = new ItemRequirement("Dirty note", ItemID.TGOD_HINT_NOTE);
		dirtyNote2 = new ItemRequirement("Dirty note", ItemID.TGOD_VINE_NOTE);
		dirtyNote3 = new ItemRequirement("Dirty note", ItemID.TGOD_STRANGLE_NOTE);

		compass = new ItemRequirement("Compass", ItemID.TGOD_GARDEN_3_COMPASS);
		warningNote = new ItemRequirement("Warning note", ItemID.TGOD_FINAL_NOTE);
	}

	@Override
	protected void setupZones()
	{
		hole = new Zone(new WorldPoint(1290, 9860, 0), new WorldPoint(1325, 9895, 0));
		molchIsland = new Zone(new WorldPoint(1358, 3623, 0), new WorldPoint(1379, 3643, 0));
		molchHole = new Zone(new WorldPoint(1362, 9990, 0), new WorldPoint(1388, 10036, 0));
		xericHole = new Zone(new WorldPoint(1292, 9995, 0), new WorldPoint(1319, 10039, 0));
		morraHole = new Zone(new WorldPoint(1419, 9812, 0), new WorldPoint(1460, 9836, 0));
	}

	public void setupConditions()
	{
		inHole = new ZoneRequirement(hole);
		onMolchIsland = new ZoneRequirement(molchIsland);
		inMolchHole = new ZoneRequirement(molchHole);
		inXericHole = new ZoneRequirement(xericHole);
		inMorraHole = new ZoneRequirement(morraHole);

		translationOpen = new WidgetTextRequirement(804, 4, "Translations");
		chatInputOpen = new Conditions(LogicType.OR,
			new WidgetTextRequirement(InterfaceID.Chatbox.MES_TEXT, "Enter a possible translation for a word you've found:"),
			new WidgetTextRequirement(229, 1, "You've discovered a new translation",
				"You've already discovered this translation", "You can't think of any suitable translations"),
			new WidgetTextRequirement(219, 1, 0, "Attempt another translation?")
		);

		searchedVines = new VarbitRequirement(QuestVarbits.QUEST_THE_GARDEN_OF_DEATH.getId(), 22, Operation.GREATER_EQUAL);
		hasCutVines = new VarbitRequirement(QuestVarbits.QUEST_THE_GARDEN_OF_DEATH.getId(), 24, Operation.GREATER_EQUAL);

		hasReadTablet2 = new VarbitRequirement(QuestVarbits.QUEST_THE_GARDEN_OF_DEATH.getId(), 28, Operation.GREATER_EQUAL);
		hasReadTablet3 = new VarbitRequirement(QuestVarbits.QUEST_THE_GARDEN_OF_DEATH.getId(), 38, Operation.GREATER_EQUAL);
		hasReadTablet4 = new VarbitRequirement(QuestVarbits.QUEST_THE_GARDEN_OF_DEATH.getId(), 48, Operation.GREATER_EQUAL);
	}

	public void setupSteps()
	{
		getJournal = new ObjectStep(this, ObjectID.TGOD_TENT, new WorldPoint(1314, 3470, 0), "Search the tent in the south east of the Kebos Lowlands.");
		getJournal.addDialogStep("Yes.");
		readJournal = new DetailedQuestStep(this, "Read the journal", kasondesJournal.highlighted());
		getSecateurs = new ObjectStep(this, ObjectID.TGOD_CAMPING_EQUIPMENT, new WorldPoint(1312, 3470, 0), "Search the camping equipment for some secateurs.");
		enterHole = new ObjectStep(this, ObjectID.TGOD_GARDEN_1_ENTRY, new WorldPoint(1308, 3467, 0), "Enter the nearby hole.", secateurs);
		searchForTablet = new ObjectStep(this, ObjectID.TGOD_GARDEN_1_TABLET_TABLE, new WorldPoint(1306, 9885, 0), "Search the north-west table.");
		readTablet1 = new DetailedQuestStep(this, "Read the stone tablet.", stoneTablet1.highlighted());
		readTablet1.addDialogStep("Yes.");
		readTranslations = new DetailedQuestStep(this, "Read the word translations.", wordTranslations.highlighted());
		attemptTranslation = new WidgetStep(this, "Click the 'Attempt Translation' button.", 804, 5);

		inputWords1 = new PuzzleWrapperStep(this, new DetailedQuestStep(this, "Type 'Island', 'Water', 'Time', 'Vessel', and 'North'."), "Work out various translation words and input them to the word translations.");
		inputWords1.addDialogStep("Yes.");

		leaveHole = new ObjectStep(this, ObjectID.TGOD_GARDEN_1_EXIT, new WorldPoint(1309, 9867, 0), "Leave the hole.");
		goToMolch = new ObjectStep(this, ObjectID.AERIAL_FISHING_BOAT, new WorldPoint(1406, 3612, 0), "Travel to Molch island via Boaty, located on the west/north/east sides of Lake Molch.", wordTranslations, secateurs);
		goToMolch.addDialogStep("Molch Island");
		enterMolchHole = new ObjectStep(this, ObjectID.TGOD_GARDEN_2_ENTRY, new WorldPoint(1364, 3637, 0), "Enter the hole on Molch Island.", wordTranslations, secateurs);
		searchVines = new ObjectStep(this, ObjectID.TGOD_VINES, new WorldPoint(1375, 10024, 0), "Inspect the vines to the south.");
		cutVines = new ObjectStep(this, ObjectID.TGOD_VINES, new WorldPoint(1375, 10024, 0), "Cut the vines with secateurs.", secateurs.highlighted());
		cutVines.addIcon(ItemID.SECATEURS);

		searchForTablet2 = new ObjectStep(this, ObjectID.TGOD_GARDEN_2_TABLET_TABLE, new WorldPoint(1373, 10014, 0), "Search the south west stone table.");
		readTablet2 = new DetailedQuestStep(this, "Read the stone tablet, and take the note after closing the tablet's interface.", stoneTablet2.highlighted());
		readTablet2.addDialogStep("Yes.");
		readTranslations2 = new DetailedQuestStep(this, "Read the word translations.", wordTranslations.highlighted());
		attemptTranslation2 = new WidgetStep(this, "Click the 'Attempt Translation' button.", 804, 5);
		inputWords2 = new PuzzleWrapperStep(this, new DetailedQuestStep(this, "Type 'West', 'Poison', 'Body', 'Food', and 'Earth'."),
			"Work out various translation words and input them to the word translations.");
		inputWords2.addDialogStep("Yes.");

		leaveHole2 = new ObjectStep(this, ObjectID.TGOD_GARDEN_2_EXIT, new WorldPoint(1375, 10033, 0), "Leave the hole.");
		leaveMolchIsland = new ObjectStep(this, ObjectID.AERIAL_FISHING_BOAT, new WorldPoint(1369, 3641, 0), "Travel via Boaty to Molch, and enter the hole near Xeric's Shrine.", wordTranslations);
		leaveMolchIsland.addDialogStep("Molch");
		enterXericShrineHole = new ObjectStep(this, ObjectID.TGOD_GARDEN_3_ENTRY, new WorldPoint(1314, 3617, 0), "Enter the hole next to Xeric's Shrine.", wordTranslations);
		searchForTablet3 = new ObjectStep(this, ObjectID.TGOD_GARDEN_3_TABLET_TABLE, new WorldPoint(1311, 10018, 0), "Search the south east stone table.");
		readTablet3 = new DetailedQuestStep(this, "Read the stone tablet.", stoneTablet3.highlighted());
		readTablet3.addDialogStep("Yes.");
		readTranslations3 = new DetailedQuestStep(this, "Read the word translations.", wordTranslations.highlighted());
		attemptTranslation3 = new WidgetStep(this, "Click the 'Attempt Translation' button.", 804, 5);
		inputWords3 = new PuzzleWrapperStep(this, new DetailedQuestStep(this, "Type 'Make', 'Yes', 'No', 'Move', 'Arrive', 'East', and 'South'."),
			"Work out various translation words and input them to the word translations.");
		inputWords3.addDialogStep("Yes.");

		leaveHole3 = new ObjectStep(this, ObjectID.TGOD_GARDEN_3_EXIT, new WorldPoint(1294, 10033, 0), "Leave the hole.");
		enterMorraHole = new ObjectStep(this, ObjectID.TGOD_GARDEN_4_ENTRY, new WorldPoint(1450, 3511, 0),
			"Enter the hole in the Ruins of Morra, south west of Shayzien.", wordTranslations);
		searchForTablet4 = new ObjectStep(this, ObjectID.TGOD_GARDEN_4_TABLET_TABLE, new WorldPoint(1440, 9816, 0),
			"Search the south stone table.");
		readTablet4 = new DetailedQuestStep(this, "Read the stone tablet.", stoneTablet4.highlighted());
		readTablet4.addDialogStep("Yes.");
		readTranslations4 = new DetailedQuestStep(this, "Read the word translations.", wordTranslations.highlighted());
		attemptTranslation4 = new WidgetStep(this, "Click the 'Attempt Translation' button.", 804, 5);
		inputWords4 = new PuzzleWrapperStep(this, new DetailedQuestStep(this, "Type 'Few', 'Big', 'Sun', 'Moon', 'Life', 'Death', 'Mind', 'Home', 'Air', and 'Fire'."),
			"Work out various translation words and input them to the word translations.");
		inputWords4.addDialogStep("Yes.");

		readTabletFinal = new DetailedQuestStep(this, "Read the final stone tablet again.", stoneTablet4.highlighted());
		readTabletFinal.addDialogStep("Yes.");
		readWarningNote = new DetailedQuestStep(this, "Read the warning note.", warningNote.highlighted());
	}

	@Override
	public List<ItemRequirement> getItemRequirements()
	{
		return Collections.singletonList(secateurs);
	}

	@Override
	public List<ItemRequirement> getItemRecommended()
	{
		return Arrays.asList(xericsTalisman, kharedstMemoir, fairyRingAccess, antipoison);
	}

	@Override
	public List<Requirement> getGeneralRequirements()
	{
		ArrayList<Requirement> req = new ArrayList<>();
		req.add(new SkillRequirement(Skill.FARMING, 20));
		return req;
	}

	@Override
	public QuestPointReward getQuestPointReward()
	{
		return new QuestPointReward(1);
	}

	@Override
	public List<ExperienceReward> getExperienceRewards()
	{
		return Collections.singletonList(new ExperienceReward(Skill.FARMING, 10000));
	}

	@Override
	public List<PanelDetails> getPanels()
	{
		List<PanelDetails> allSteps = new ArrayList<>();
		allSteps.add(new PanelDetails("Starting off",
			Arrays.asList(getJournal, readJournal, getSecateurs, enterHole, searchForTablet, readTablet1, readTranslations, attemptTranslation,
				inputWords1, leaveHole), secateurs));

		allSteps.add(new PanelDetails("Investigating Molch",
			Arrays.asList(goToMolch, enterMolchHole, searchVines, cutVines, searchForTablet2, readTablet2, readTranslations2, attemptTranslation2, inputWords2), secateurs, wordTranslations));

		allSteps.add(new PanelDetails("Investigating Xeric's Shrine",
			Arrays.asList(leaveHole2, leaveMolchIsland, enterXericShrineHole, searchForTablet3, readTablet3, readTranslations3, attemptTranslation3, inputWords3), wordTranslations));


		allSteps.add(new PanelDetails("Investigating the Ruins of Morra",
			Arrays.asList(leaveHole3, enterMorraHole, searchForTablet4, readTablet4, readTranslations4, attemptTranslation4, inputWords4, readTabletFinal, readWarningNote)));

		return allSteps;
	}
}
