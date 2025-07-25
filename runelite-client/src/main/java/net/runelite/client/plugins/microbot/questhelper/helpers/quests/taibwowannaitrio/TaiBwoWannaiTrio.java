/*
 * Copyright (c) 2021, Zoinkwiz
 * Copyright (c) 2021, itofu1
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
package net.runelite.client.plugins.microbot.questhelper.helpers.quests.taibwowannaitrio;

import net.runelite.client.plugins.microbot.questhelper.bank.banktab.BankSlotIcons;
import net.runelite.client.plugins.microbot.questhelper.collections.ItemCollections;
import net.runelite.client.plugins.microbot.questhelper.panel.PanelDetails;
import net.runelite.client.plugins.microbot.questhelper.questhelpers.BasicQuestHelper;
import net.runelite.client.plugins.microbot.questhelper.questinfo.QuestHelperQuest;
import net.runelite.client.plugins.microbot.questhelper.questinfo.QuestVarPlayer;
import net.runelite.client.plugins.microbot.questhelper.requirements.Requirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.conditional.Conditions;
import net.runelite.client.plugins.microbot.questhelper.requirements.conditional.NpcCondition;
import net.runelite.client.plugins.microbot.questhelper.requirements.conditional.ObjectCondition;
import net.runelite.client.plugins.microbot.questhelper.requirements.item.ItemOnTileRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.item.ItemRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.item.ItemRequirements;
import net.runelite.client.plugins.microbot.questhelper.requirements.npc.DialogRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.player.SkillRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.quest.QuestRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.util.LogicType;
import net.runelite.client.plugins.microbot.questhelper.requirements.var.VarplayerRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.widget.WidgetTextRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.zone.Zone;
import net.runelite.client.plugins.microbot.questhelper.requirements.zone.ZoneRequirement;
import net.runelite.client.plugins.microbot.questhelper.rewards.ExperienceReward;
import net.runelite.client.plugins.microbot.questhelper.rewards.ItemReward;
import net.runelite.client.plugins.microbot.questhelper.rewards.QuestPointReward;
import net.runelite.client.plugins.microbot.questhelper.rewards.UnlockReward;
import net.runelite.client.plugins.microbot.questhelper.steps.*;
import net.runelite.api.QuestState;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.gameval.NpcID;
import net.runelite.api.gameval.ObjectID;

import java.util.*;

public class TaiBwoWannaiTrio extends BasicQuestHelper
{
	ItemRequirement hammer, slicedBanana, banana, knife, slicedBananaOrKnife, smallFishingNet, pestleAndMortar, spear,
		agilityPotion4, rangedOrMagic, tinderbox, jogreBones, burntJogreBones, pastyJogreBones, marinatedJogreBones,
		anyJogreBones, seaweed, karamjanRum, karambwanji, poisonedSpear, logsForFire, rawKarambwans, coins, karambwanVessel,
		filledKarabmwanVessel, bananaSlices, karamjanRumWithBanana, monkeyCorpse, karambwanPaste, poisonKarambwan,
		karambwanjiPaste, monkeySkin, seaweedSandwich, craftingManual, rawKarambwan;

	QuestStep goToTimfrakuLadder, talkToTimfrakuStart, syncStep, fishKarambwaji, goToLubufu, dropVessel, getAnotherVessel, pickupVessel,
		getMoreVessel, fillVessel, getRum, sliceBanana, makeBananaRum, talkToTiadeche1, giveVessel, getJogreBones,
		getMonkeyCorpse, talkToTamayu1, pickupSeaweed, pickupBones, pickupCorpse, poisonSpear, givePotion, giveSpear,
		cookKarambwan, burnBones, cookBones, useCorpseOnTamayu, makeSeaweedSandwich, talkToTinsay, goToTiadecheFinal,
		goToTimfrakuLadderEnd, talkToTimfrakuEnd, pickupBurntBones;

	QuestStep makeKarambwanjiPaste, usePasteOnSpear, usePestleOnKarambwan, usePasteOnBones, getPoisonKarambwan,
	goOnHuntToKill, askAboutResearch, useVesselOnTinsay;

	Requirement startedQuestDuringSession, syncedState, inTimfrakusHut, inLufubuZone, givenKarambwanji,
		vesselOnGround,	talkedToTiadeche, givenVessel, bonesNearby, corpseNearby, wentOnHunt, givenPotion, givenSpear,
		burningBonesNearby, burntBonesNearby, hadAtLeastRawKarambwan, hadRumWithBanana, hadSeaweed, hadMarinated,
		defeatedBeast, talkedTinsay1, givenRum, givenSandwich, givenBones, hadSeaweedSandwich,
		beenAskedToResearchVessel, hadManual;

	Zone timfrakusHut, lubufuZone;

	WorldPoint lubufuWorldPoint, timfrakuHutWorldPoint;

	@Override
	public Map<Integer, QuestStep> loadSteps()
	{
		initializeRequirements();
		setupWorldPoints();
		setupConditions();
		setupSteps();

		ConditionalStep startQuest = new ConditionalStep(this, goToTimfrakuLadder);
		startQuest.addStep(inTimfrakusHut, talkToTimfrakuStart);


		Map<Integer, QuestStep> steps = new HashMap<>();
		steps.put(0, startQuest);
		steps.put(1, startQuest);
		steps.put(2, startQuest);

		ConditionalStep coreQuest = new ConditionalStep(this, fishKarambwaji);
		coreQuest.addStep(hadManual, goToTiadecheFinal);
		coreQuest.addStep(new Conditions(beenAskedToResearchVessel, defeatedBeast, givenBones), useVesselOnTinsay);
		coreQuest.addStep(new Conditions(hadRumWithBanana, beenAskedToResearchVessel, hadMarinated, hadSeaweedSandwich), talkToTinsay);
		coreQuest.addStep(new Conditions(hadRumWithBanana, beenAskedToResearchVessel, defeatedBeast, hadSeaweed, hadMarinated, monkeySkin), makeSeaweedSandwich);
		coreQuest.addStep(new Conditions(hadRumWithBanana, beenAskedToResearchVessel, defeatedBeast, hadSeaweed, hadMarinated, monkeyCorpse), useCorpseOnTamayu);
		coreQuest.addStep(new Conditions(hadRumWithBanana, beenAskedToResearchVessel, defeatedBeast, hadSeaweed, hadMarinated,
				corpseNearby), pickupCorpse);
		coreQuest.addStep(new Conditions(hadRumWithBanana, beenAskedToResearchVessel, defeatedBeast, hadSeaweed, hadMarinated), getMonkeyCorpse);
		coreQuest.addStep(new Conditions(hadRumWithBanana, beenAskedToResearchVessel, hadSeaweed, hadMarinated, givenPotion, givenSpear), goOnHuntToKill);
		coreQuest.addStep(new Conditions(hadRumWithBanana, beenAskedToResearchVessel, hadSeaweed, hadMarinated, poisonedSpear, givenPotion),
			giveSpear);
		coreQuest.addStep(new Conditions(hadRumWithBanana, beenAskedToResearchVessel, hadSeaweed, hadMarinated, poisonedSpear, wentOnHunt),
			givePotion);
		coreQuest.addStep(new Conditions(hadRumWithBanana, beenAskedToResearchVessel, hadSeaweed, hadMarinated, poisonedSpear), talkToTamayu1);

		// Collect last bits
		coreQuest.addStep(new Conditions(hadRumWithBanana, beenAskedToResearchVessel, hadSeaweed, hadMarinated, karambwanPaste),
			usePasteOnSpear);
		coreQuest.addStep(new Conditions(hadRumWithBanana, beenAskedToResearchVessel, hadSeaweed, hadMarinated, poisonKarambwan),
			usePestleOnKarambwan);
		coreQuest.addStep(new Conditions(hadRumWithBanana, beenAskedToResearchVessel, hadSeaweed, hadMarinated, hadAtLeastRawKarambwan),
			cookKarambwan);
		coreQuest.addStep(new Conditions(hadRumWithBanana, beenAskedToResearchVessel, hadSeaweed, pastyJogreBones, hadAtLeastRawKarambwan),
			cookBones);
		coreQuest.addStep(new Conditions(hadRumWithBanana, beenAskedToResearchVessel, hadSeaweed, pastyJogreBones), getPoisonKarambwan);
		coreQuest.addStep(new Conditions(hadRumWithBanana, beenAskedToResearchVessel, hadSeaweed, burntJogreBones, karambwanjiPaste),
			usePasteOnBones);
		coreQuest.addStep(new Conditions(hadRumWithBanana, beenAskedToResearchVessel, hadSeaweed, burntJogreBones),
			makeKarambwanjiPaste);
		coreQuest.addStep(new Conditions(hadRumWithBanana, beenAskedToResearchVessel, hadSeaweed, new Conditions(LogicType.OR,
				burningBonesNearby, burntBonesNearby)), pickupBurntBones);
		coreQuest.addStep(new Conditions(hadRumWithBanana, beenAskedToResearchVessel, hadSeaweed, anyJogreBones), burnBones);
		coreQuest.addStep(new Conditions(hadRumWithBanana, beenAskedToResearchVessel, hadSeaweed, bonesNearby), pickupBones);
		coreQuest.addStep(new Conditions(hadRumWithBanana, beenAskedToResearchVessel, hadSeaweed), getJogreBones);
		coreQuest.addStep(new Conditions(hadRumWithBanana, beenAskedToResearchVessel), pickupSeaweed);

		// Tiadeche
		coreQuest.addStep(new Conditions(filledKarabmwanVessel.quantity(2), karamjanRumWithBanana, givenVessel), askAboutResearch);
		coreQuest.addStep(new Conditions(filledKarabmwanVessel.quantity(2), karamjanRumWithBanana, talkedToTiadeche), giveVessel);
		coreQuest.addStep(new Conditions(filledKarabmwanVessel.quantity(2), karamjanRumWithBanana), talkToTiadeche1);
		coreQuest.addStep(new Conditions(filledKarabmwanVessel.quantity(2), karamjanRum, bananaSlices), makeBananaRum);
		coreQuest.addStep(new Conditions(filledKarabmwanVessel.quantity(2), karamjanRum), sliceBanana);
		coreQuest.addStep(new Conditions(filledKarabmwanVessel.quantity(2)), getRum);
		coreQuest.addStep(new Conditions(karambwanVessel.quantity(2)), fillVessel);
		coreQuest.addStep(new Conditions(vesselOnGround, karambwanVessel), pickupVessel);
		coreQuest.addStep(new Conditions(vesselOnGround), getAnotherVessel);
		coreQuest.addStep(new Conditions(karambwanVessel), dropVessel);
		coreQuest.addStep(new Conditions(LogicType.OR, givenKarambwanji, karambwanVessel, vesselOnGround), getMoreVessel);
		coreQuest.addStep(karambwanji.quantity(23), goToLubufu);

		ConditionalStep stepsWithSyncState = new ConditionalStep(this, syncStep);
		stepsWithSyncState.addStep(syncedState, coreQuest);
		steps.put(3, stepsWithSyncState);

		ConditionalStep endQuest = new ConditionalStep(this, goToTimfrakuLadderEnd);
		endQuest.addStep(inTimfrakusHut, talkToTimfrakuEnd);
		steps.put(4, endQuest);
		steps.put(5, endQuest);

		return steps;
	}

	private void setupWorldPoints()
	{
		timfrakuHutWorldPoint = new WorldPoint(2782,3087,0);
		lubufuWorldPoint = new WorldPoint(2769,3171,0);
	}

	private void setupSteps()
	{
		goToTimfrakuLadder = new ObjectStep(this, ObjectID.LADDER, timfrakuHutWorldPoint,
			"Talk to Timfraku upstairs in his house in Tai Bwo Wannai.");

		talkToTimfrakuStart = new NpcStep(this, NpcID.TBWT_TIMFRAKU, "Talk to Timfraku upstairs in his house in Tai Bwo Wannai.");
		talkToTimfrakuStart.addDialogSteps("I am a roving adventurer.", "I am a travelling explorer.", "I am a " +
			"wandering wayfarer.", "Who me? Oh I'm just a nobody.");
		talkToTimfrakuStart.addDialogStep("Trufitus sent me.");
		talkToTimfrakuStart.addDialogSteps("Your gratitude is all I deserve.", "Well, some gold would be nice.", "So " +
			"far??", "Yes.");
		talkToTimfrakuStart.addSubSteps(goToTimfrakuLadder);

		syncStep = new DetailedQuestStep(this, "Open your quest journal to sync your current state.");

		fishKarambwaji = new NpcStep(this, NpcID._0_43_47_KARAMBWANJI, new WorldPoint(2791,3019,0),
			"Using your small fishing net, catch atleast 23 raw karambwanji just south of Tai Bwo Wannai.", smallFishingNet);

		goToLubufu = new NpcStep(this, NpcID.TBWT_LUBUFU, lubufuWorldPoint, "Go to Brimhaven and talk to Lubufu. " +
			"You have to talk to him multiple times. You'll need to ask him twice about what he does, then talk to " +
			"him to give him the karambwanji.", karambwanji.quantity(20));
		goToLubufu.addDialogStep("Talk about him...");
		goToLubufu.addDialogStep("What do you do?");
		goToLubufu.addDialogStepWithExclusion("What do you do with your Karambwan?", "I could help collect the bait.");
		goToLubufu.addDialogStep("I could help collect the bait.");
		goToLubufu.addDialogStep("You sound like you could do with the help.");
		goToLubufu.addDialogStep("What do you use to catch Karambwan?");
		goToLubufu.addDialogStep("Yes!");

		getMoreVessel = new NpcStep(this, NpcID.TBWT_LUBUFU, lubufuWorldPoint,
			"Get a Vessel from Lubufu by asking what he uses to catch them multiple times. Drop it, and talk to him " +
				"go get another, then pick up the one you dropped. You can get as many as you want but you need 2 to" +
				" complete this quest.");
		getMoreVessel.addDialogStep("What do you use to catch Karambwan?");
		getMoreVessel.addDialogStep("Yes!");
		getMoreVessel.addDialogStep("Actually, I've lost my Karambwan vessel.");
		getMoreVessel.addDialogStep("... a shark ate it!");

		dropVessel = new DetailedQuestStep(this, "Drop the vessel, then get another from Lubufu.", karambwanVessel.highlighted());
		getAnotherVessel = new NpcStep(this, NpcID.TBWT_LUBUFU, lubufuWorldPoint,
			"Get another Vessel from Lubufu.");
		getAnotherVessel.addDialogStep("Actually, I've lost my Karambwan vessel.");
		getAnotherVessel.addDialogStep("... a shark ate it!");
		pickupVessel = new ItemStep(this, "Pick up the vessel you dropped.", karambwanVessel.quantity(2));
		getMoreVessel.addSubSteps(dropVessel, getAnotherVessel, pickupVessel);

		fillVessel = new DetailedQuestStep(this, "Use a karambwanji on a karambwan Vessel to fill it.", karambwanji, karambwanVessel);

		getRum = new NpcStep(this, NpcID.ZEMBO, new WorldPoint(2925, 3143, 0),
			"Go east to Musa point to buy some Karamjan rum from Zembo.", coins.quantity(30));

		sliceBanana = new DetailedQuestStep(this, "Slice a banana with a knife. You can get a banana from one of the " +
			"trees in the plantation.", knife.highlighted(), banana.highlighted());
		makeBananaRum = new ItemStep(this, "Add banana slices to the karamjan rum to make Karamjan rum with banana " +
			"slices.", bananaSlices.highlighted(), karamjanRum.highlighted());

		talkToTiadeche1 = new NpcStep(this, NpcID.TBWT_TIADECHE, new WorldPoint(2912, 3116, 0),
			"Talk to Tiadeche in east Karamja, near the fairy ring DKP. You can teleport off Karamja; the Karamjan rum with banana will not break.");

		giveVessel = new NpcStep(this, NpcID.TBWT_TIADECHE, new WorldPoint(2912, 3116, 0),
			"Use a filled vessel on Tiadeche. Make sure to finish the dialogue with him and accept the karambwan he " +
				"offers!",	filledKarabmwanVessel.highlighted());
		giveVessel.addDialogStep("Yes");
		giveVessel.addIcon(ItemID.TBWT_KARAMBWAN_VESSEL_LOADED_WITH_KARAMBWANJI);

		askAboutResearch = new NpcStep(this, NpcID.TBWT_TIADECHE, new WorldPoint(2912, 3116, 0),
			"Talk to Tiadeche more.");

		pickupSeaweed = new ItemStep(this, "Pickup some seaweed from Karamja's coast.", seaweed);
		pickupSeaweed.addDialogStep("Yes");
		getJogreBones = new NpcStep(this, NpcID.JOGRE, new WorldPoint(2925, 3062, 0),
			"Get some jogre bones. You can kill the jogres south of Tiadeche.", true, jogreBones);
		getJogreBones.addDialogStep("Yes");
		pickupBones = new ItemStep(this, "Pickup the jogre bones.", jogreBones);
		getJogreBones.addSubSteps(pickupBones);

		getMonkeyCorpse = new NpcStep(this, NpcID.MONKEY,
			"Kill a monkey to get its corpse, there are plenty of monkeys around Karamja and you will need either " +
				"magic or range to kill one.", true, rangedOrMagic);
		pickupCorpse = new ItemStep(this, "Pickup the monkey corpse.", monkeyCorpse);
		getMonkeyCorpse.addSubSteps(pickupCorpse);

		talkToTamayu1 = new NpcStep(this, NpcID.TBWT_TAMAYU, new WorldPoint(2845, 3041, 0),
			"Talk to Tamayu, he is located north of Shilo Village near the mining sign on the map," +
				" after watching him use your tinder box on your jogre bones and pick up the burnt jogre bones.");
		talkToTamayu1.addDialogStep("When will you succeed?");
		talkToTamayu1.addDialogStep("Yes");

		poisonSpear = new DetailedQuestStep(this, "Use the paste on an iron or better spear.", karambwanPaste.highlighted(),
			spear.highlighted());
		givePotion = new NpcStep(this, NpcID.TBWT_TAMAYU, new WorldPoint(2845, 3041, 0),
			"Use an agility potion (4) on Tamayu.", agilityPotion4.highlighted());
		givePotion.addIcon(ItemID._4DOSE1AGILITY);
		giveSpear = new NpcStep(this, NpcID.TBWT_TAMAYU, new WorldPoint(2845, 3041, 0),
			"Use a poisoned spear on Tamayu.", poisonedSpear.highlighted());
		giveSpear.addIcon(ItemID.TBWT_IRON_SPEAR_KP);

		getPoisonKarambwan = new DetailedQuestStep(this, "Fish raw Karambwan next to Tiadeche with a loaded vessel or" +
			" buy them from the GE.");

		makeKarambwanjiPaste = new DetailedQuestStep(this, "Use a pestle and mortar on a karambwanji.",
			pestleAndMortar.highlighted(), karambwanji.highlighted());
		usePasteOnBones = new DetailedQuestStep(this, "Use the karambwanji paste on the burnt jogre bones.",
			karambwanjiPaste.highlighted(), burntJogreBones.highlighted());

		usePestleOnKarambwan = new DetailedQuestStep(this, "Use a pestle and mortar on a poison karambwan.",
			pestleAndMortar.highlighted(), poisonKarambwan.highlighted());
		usePasteOnSpear = new DetailedQuestStep(this, "Use the karambwan paste on a iron or better spear.",
			karambwanPaste.highlighted(), spear.highlighted());

		pickupBurntBones = new ItemStep(this, "Wait for the bones to burn, then pick them up.", burntJogreBones);


		cookKarambwan = new DetailedQuestStep(this,
			"Cook the karambwan for a poison karambwan. Note you may fail this and need to try again.");
		burnBones = new DetailedQuestStep(this,
			"Use a tinderbox on the jogre bones to set them on fire.", tinderbox.highlighted(), jogreBones.highlighted());
		burnBones.addDialogStep("Yes");

		goOnHuntToKill = new NpcStep(this, NpcID.TBWT_TAMAYU, new WorldPoint(2845, 3041, 0),
			"Go on a hunt with Tamayu.");
		goOnHuntToKill.addDialogStep("Take me on your next hunt for the Shaikahan.");

		cookBones = new ObjectStep(this, ObjectID.FIRE, new WorldPoint(2790, 3048, 0),
			"RIGHT-CLICK use the bones on a fire. You can either make one yourself, or use them on the fire " +
				"south of Tai Bwo Wannai.", pastyJogreBones.highlighted());
		cookBones.addIcon(ItemID.TBWT_BURNT_JOGRE_BONES_IN_RAW_KARAMBWANJI_PASTE);

		useCorpseOnTamayu = new NpcStep(this, NpcID.TBWT_TAMAYU, new WorldPoint(2845, 3041, 0),
			"Use your monkey corpse on Tamayu to have him skin it for you.", monkeyCorpse.highlighted());
		useCorpseOnTamayu.addIcon(ItemID.TBWT_MONKEY_CORPSE);

		makeSeaweedSandwich = new DetailedQuestStep(this, "Use your seaweed on your monkey skin to make a seaweed " +
			"sandwich.", seaweed.highlighted(), monkeySkin.highlighted());

		talkToTinsay = new NpcStep(this, NpcID.TBWT_TINSAY, new WorldPoint(2764, 2975, 0),
			"Talk to Tinsay on Cairn Isle west of Shilo Village, south of fairy ring CKR. Keep talking to him, giving" +
				" him the items he asks for.");
		talkToTinsay.addDialogStep("Yes.");

		useVesselOnTinsay = new NpcStep(this, NpcID.TBWT_TINSAY, new WorldPoint(2764, 2975, 0),
			"Use the vessel on Tinsay.", karambwanVessel.highlighted());
		useVesselOnTinsay.addIcon(ItemID.TBWT_KARAMBWAN_VESSEL);

		goToTiadecheFinal = new NpcStep(this, NpcID.TBWT_TIADECHE, new WorldPoint(2912, 3116, 0),
			"Go back to Tiadeche located north of fairy ring DKP and use the crafting manual on him.");

		goToTimfrakuLadderEnd = new ObjectStep(this, ObjectID.LADDER, timfrakuHutWorldPoint,
			"Go to Timfraku's house to finish the quest.");
		talkToTimfrakuEnd = new NpcStep(this, NpcID.TBWT_TIMFRAKU,
			"Talk to Timfraku to end the quest, the option chosen here does not matter. " +
				"NOTE: you will need to talk to each of the brothers individually in Tai Bwo Wannai to receive " +
				"experience rewards as well as the ability to cook karambwans properly.");
		talkToTimfrakuEnd.addDialogSteps("Oh it was nothing really.", "I'd rather have some gold please.",
			"You know your sons have serious issues...", "Eternal gratitude accepted.");
	}


	private void setupConditions()
	{
		inTimfrakusHut = new ZoneRequirement(timfrakusHut);
		inLufubuZone = new ZoneRequirement(lubufuZone);

		startedQuestDuringSession = new Conditions(true,
			new VarplayerRequirement(QuestVarPlayer.QUEST_TAI_BWO_WANNAI_TRIO.getId(), 2));

		syncedState = new Conditions(true, LogicType.OR,
			new WidgetTextRequirement(InterfaceID.Questjournal.TITLE, "Tai Bwo Wannai Trio"),
			startedQuestDuringSession
		);

		givenVessel = new Conditions(true, LogicType.OR,
			new WidgetTextRequirement(InterfaceID.Questjournal.TEXTLAYER, true, "<str>He has successfully caught a Karambwan."),
			new WidgetTextRequirement(InterfaceID.Objectbox.TEXT, "You hand over the Karambwan vessel to Tiadeche."),
			new DialogRequirement("What is it?")
		);

		givenKarambwanji = new Conditions(true, LogicType.OR,
			givenVessel,
			new WidgetTextRequirement(193, 2, "You hand Lubufu 20 raw Karambwanji."),
			new WidgetTextRequirement(InterfaceID.Questjournal.TEXTLAYER, true, "<str>I have given Lubufu 20 Karambwanji.")
		);

		vesselOnGround = new ItemOnTileRequirement(karambwanVessel);

		talkedToTiadeche = new Conditions(true, LogicType.OR,
			givenVessel,
			new DialogRequirement("I will return only when I have caught a Karambwan."),
			new WidgetTextRequirement(219, 1, 4, "How are you fishing for the Karambwan?"),
			new WidgetTextRequirement(InterfaceID.Questjournal.TEXTLAYER, true, "<col=000080>He will only return to the village once he has caught a")
		);

		beenAskedToResearchVessel = new Conditions(true, LogicType.OR,
			new DialogRequirement("Take a Karambwan vessel to my brother Tinsay."),
			new WidgetTextRequirement(InterfaceID.Questjournal.TEXTLAYER, true, "<col=000080>I must take a <col=800000>Karambwan vessel<col=000080> to <col=800000>Tinsay<col=000080> and retrieve")
		);

		bonesNearby = new ItemOnTileRequirement(ItemID.TBWT_JOGRE_BONES);
		corpseNearby = new ItemOnTileRequirement(ItemID.TBWT_MONKEY_CORPSE);

		burningBonesNearby = new ObjectCondition(ObjectID.BONES_IN_PASTE_FIRE);
		burntBonesNearby = new ItemOnTileRequirement(ItemID.TBWT_BURNT_JOGRE_BONES);

		wentOnHunt = new Conditions(true, LogicType.OR,
			new NpcCondition(NpcID.TBWT_TAMAYU_HUNTER),
			new DialogRequirement("I simply cannot match the Shaikahan's agility!",
				"I cannot do enough damage with this spear..."),
			new WidgetTextRequirement(InterfaceID.Questjournal.TEXTLAYER, true, "He appears to be having difficulty in the hunt.")
		);

		givenPotion = new Conditions(true, LogicType.OR,
			new WidgetTextRequirement(InterfaceID.Objectbox.TEXT, "You hand over the agility potion to Tamayu."),
			new DialogRequirement("Thank you Bwana. Now I must prepare for my next"),
			new WidgetTextRequirement(InterfaceID.Questjournal.TEXTLAYER, true, "<str>I have increased his agility to match the Shaikahan's.")
		);
		givenSpear = new Conditions(true, LogicType.OR,
			new DialogRequirement("Tamayu, try using this weapon."),
			new WidgetTextRequirement(InterfaceID.Objectbox.TEXT, "You hand the spear to Tamayu."),
			new WidgetTextRequirement(InterfaceID.Questjournal.TEXTLAYER, true, "<str>I have give him a stronger and Karambwan poisoned spear.")
		);

		defeatedBeast = new Conditions(true, LogicType.OR,
			new DialogRequirement("I did it! I, Tamayu, first son of Timfraku, did slay " +
				"the Shaikahan!"),
			new DialogRequirement("The deaths of my kin have been avenged. You are my witness."),
			new WidgetTextRequirement(InterfaceID.Questjournal.TEXTLAYER, true, "<str>Tamayu has slain the Shaikahan!")
		);

		hadAtLeastRawKarambwan = new Conditions(LogicType.OR, rawKarambwan, poisonKarambwan, karambwanPaste,
			poisonedSpear, givenSpear);


		givenBones = new Conditions(true, LogicType.OR,
			new WidgetTextRequirement(InterfaceID.Objectbox.TEXT, "You hand Tinsay the burnt Jogre bones marinated"),
			new WidgetTextRequirement(InterfaceID.Questjournal.TEXTLAYER, true, "<str>I have given him a burnt Jogre bones marinated in"),
			new DialogRequirement("Finally! A near lifetime of craving satisfied!")
		);

		givenSandwich = new Conditions(true, LogicType.OR,
			new WidgetTextRequirement(InterfaceID.Objectbox.TEXT, "You hand Tinsay the seaweed in monkey skin sandwich."),
			new WidgetTextRequirement(InterfaceID.Questjournal.TEXTLAYER, true, "<str>I have given him a seaweed in monkey skin sandwich."),
			new DialogRequirement("Yes ... perfect! You really do not understand how necessary that was."),
			givenBones
		);

		givenRum = new Conditions(true, LogicType.OR,
			new WidgetTextRequirement(InterfaceID.Objectbox.TEXT, "You hand Tinsay the sliced bananas in Karamjan " +
				"rum."),
			new WidgetTextRequirement(InterfaceID.Questjournal.TEXTLAYER, true, "<str>I have given him sliced banana in Karamja rum."),
			new DialogRequirement("Yes ... that's it! Hits just the spot!"),
			givenSandwich,
			givenBones
		);

		hadRumWithBanana = new Conditions(LogicType.OR, karamjanRumWithBanana, givenRum);
		hadSeaweed = new Conditions(LogicType.OR, seaweed, seaweedSandwich, givenSandwich);
		hadSeaweedSandwich = new Conditions(LogicType.OR, seaweedSandwich, givenSandwich);
		hadMarinated = new Conditions(LogicType.OR, marinatedJogreBones.alsoCheckBank(questBank), givenBones);

		talkedTinsay1 = new Conditions(true, LogicType.OR,
			new WidgetTextRequirement(InterfaceID.Questjournal.TEXTLAYER, true, "<col=000080>He requires <col=800000>banana in Karamja " +
				"rum<col=000080> to repair the tribal"),
			new DialogRequirement("And you're going to use this to repair the"),
			new DialogRequirement("Hmm ... I think I need banana in Karamjan rum.")
		);

		hadManual = new Conditions(true, LogicType.OR,
			craftingManual,
			new WidgetTextRequirement(InterfaceID.Objectbox.TEXT, "You hand over the crafting manual to Tiadeche."),
			new WidgetTextRequirement(InterfaceID.Questjournal.TEXTLAYER, true, "<str>retrieved crafting instructions for Tiadeche.")
		);
	}

	@Override
	protected void setupZones()
	{
		timfrakusHut = new Zone(new WorldPoint(2778,3084,1), new WorldPoint(2786, 3090,1));
		lubufuZone = new Zone(new WorldPoint(2759,3173,0), new WorldPoint(2780,3162,0));
	}

	@Override
	protected void setupRequirements()
	{
		hammer = new ItemRequirement("Hammer", ItemCollections.HAMMER).isNotConsumed();

		slicedBanana = new ItemRequirement("Sliced Banana", ItemID.TBWT_SLICED_BANANA);
		banana = new ItemRequirement("Banana", ItemID.BANANA);
		knife = new ItemRequirement("Knife", ItemID.KNIFE).isNotConsumed();
		knife.setTooltip("There's one on the counter in the Musa Point general store");
		slicedBananaOrKnife = new ItemRequirements(LogicType.OR, "Sliced banana or a knife", slicedBanana, knife);

		smallFishingNet = new ItemRequirement("Small Fishing Net", ItemID.NET).isNotConsumed();
		pestleAndMortar = new ItemRequirement("Pestle And Mortar", ItemID.PESTLE_AND_MORTAR).isNotConsumed();
		logsForFire = new ItemRequirement("Any logs to make a fire", ItemCollections.LOGS_FOR_FIRE);
		spear = new ItemRequirement("Iron spear or better (You will lose the spear)", ItemID.IRON_SPEAR);
		spear.setTooltip("Bone and black spear does NOT work");
		spear.addAlternates(ItemID.STEEL_SPEAR, ItemID.MITHRIL_SPEAR, ItemID.ADAMANT_SPEAR, ItemID.RUNE_SPEAR,
			ItemID.DRAGON_SPEAR);
		poisonedSpear = new ItemRequirement("Iron spear or better (kp) (You will lose the spear)", ItemID.TBWT_IRON_SPEAR_KP);
		poisonedSpear.addAlternates(ItemID.TBWT_STEEL_SPEAR_KP, ItemID.TBWT_MITHRIL_SPEAR_KP, ItemID.TBWT_ADAMANT_SPEAR_KP, ItemID.TBWT_RUNE_SPEAR_KP,
			ItemID.TBWT_DRAGON_SPEAR_KP);

		agilityPotion4 = new ItemRequirement("Agility Potion (4)", ItemID._4DOSE1AGILITY);
		rangedOrMagic = new ItemRequirement("Ranged or Magic equipment to kill a level 3 monkey", -1, -1).isNotConsumed();
		rangedOrMagic.setDisplayItemId(BankSlotIcons.getMagicCombatGear());
		tinderbox = new ItemRequirement("Tinderbox", ItemID.TINDERBOX).isNotConsumed();

		jogreBones = new ItemRequirement("Jogre Bones", ItemID.TBWT_JOGRE_BONES);
		jogreBones.canBeObtainedDuringQuest();
		rawKarambwan = new ItemRequirement("Raw karambwan", ItemID.TBWT_RAW_KARAMBWAN);
		rawKarambwan.setTooltip("You can obtain during quest with 65 Fishing.");
		karambwanPaste = new ItemRequirement("Karambwan paste", ItemID.TBWT_RAW_KARAMBWAN_PASTE);
		karambwanPaste.addAlternates(ItemID.TBWT_POISONOUS_KARAMBWAN_PASTE, ItemID.TBWT_COOKED_KARAMBWAN_PASTE);
		burntJogreBones = new ItemRequirement("Burnt Jogre Bones", ItemID.TBWT_BURNT_JOGRE_BONES);
		pastyJogreBones = new ItemRequirement("Pasty Jogre Bones", ItemID.TBWT_BURNT_JOGRE_BONES_IN_RAW_KARAMBWANJI_PASTE);
		marinatedJogreBones = new ItemRequirement("Marinated Jogre Bones", ItemID.TBWT_BURNT_JOGRE_BONES_MARINATED_IN_KARAMBWANJI);
		anyJogreBones = new ItemRequirement("Jogre Bones", ItemID.TBWT_JOGRE_BONES);
		anyJogreBones.addAlternates(ItemID.TBWT_BURNT_JOGRE_BONES_IN_RAW_KARAMBWANJI_PASTE, ItemID.TBWT_BURNT_JOGRE_BONES_MARINATED_IN_KARAMBWANJI);

		seaweed = new ItemRequirement("Seaweed", ItemID.SEAWEED);
		karamjanRum = new ItemRequirement("Karamjan Rum", ItemID.KARAMJA_RUM);
		karambwanji = new ItemRequirement("Or More Raw Karambwanji", ItemID.TBWT_RAW_KARAMBWANJI, 23);
		rawKarambwans = new ItemRequirement("Raw Karambwan", ItemID.TBWT_RAW_KARAMBWAN);
		coins = new ItemRequirement("Coins", ItemCollections.COINS);
		poisonKarambwan = new ItemRequirement("Poison karambwan", ItemID.TBWT_POORLY_COOKED_KARAMBWAN);
		karambwanjiPaste = new ItemRequirement("Karambwanji paste", ItemID.TBWT_RAW_KARAMBWANJI_PASTE);

		karambwanVessel = new ItemRequirement("Karambwan Vessel", ItemID.TBWT_KARAMBWAN_VESSEL);
		karambwanVessel.addAlternates(ItemID.TBWT_KARAMBWAN_VESSEL_LOADED_WITH_KARAMBWANJI);
		filledKarabmwanVessel = new ItemRequirement("Karabmwan Vessel (full)", ItemID.TBWT_KARAMBWAN_VESSEL_LOADED_WITH_KARAMBWANJI);

		bananaSlices = new ItemRequirement("Sliced Banana", ItemID.TBWT_SLICED_BANANA);
		karamjanRumWithBanana = new ItemRequirement("Karamjan Rum (with banana slices)", ItemID.TBWT_SLICED_BANANA_IN_KARAMJA_RUM);
		monkeyCorpse = new ItemRequirement("Monkey Corpse", ItemID.TBWT_MONKEY_CORPSE);
		monkeySkin = new ItemRequirement("Monkey Skin", ItemID.TBWT_MONKEY_SKIN);
		seaweedSandwich = new ItemRequirement("Seaweed Sandwich", ItemID.TBWT_SEAWEED_IN_MONKEY_SKIN_SANDWICH);

		craftingManual = new ItemRequirement("Crafting manual", ItemID.TBWT_CRAFTING_MANUAL);
	}

	@Override
	public ArrayList<ItemRequirement> getItemRequirements()
	{
		return new ArrayList<>(Arrays.asList(
			coins.quantity(30), hammer, smallFishingNet, pestleAndMortar, spear, agilityPotion4,
			rangedOrMagic, tinderbox, slicedBananaOrKnife, logsForFire, rawKarambwan
		));
	}

	@Override
	public ArrayList<ItemRequirement> getItemRecommended()
	{
		ArrayList<ItemRequirement> reqs = new ArrayList<>();
		reqs.add(jogreBones);
		reqs.add(new ItemRequirement("Extra Raw Karambwans in case you burn the one given", ItemID.TBWT_RAW_KARAMBWAN));
		reqs.add(new ItemRequirement("Any Antipoisons", ItemCollections.ANTIPOISONS));
		reqs.add(new ItemRequirement("Stamina potions", ItemCollections.STAMINA_POTIONS));
		reqs.add(new ItemRequirement("Dramen staff if you have access to fairy rings", ItemCollections.FAIRY_STAFF));
		reqs.add(new ItemRequirement("Sliced Banana (Use a knife on a banana)", ItemID.TBWT_SLICED_BANANA));
		reqs.add(new ItemRequirement("Food", -1, -1));
		return reqs;
	}

	@Override
	public List<Requirement> getGeneralRequirements()
	{
		ArrayList<Requirement> req = new ArrayList<>();
		req.add(new QuestRequirement(QuestHelperQuest.JUNGLE_POTION, QuestState.FINISHED));
		req.add(new SkillRequirement(Skill.FIREMAKING, 30, false));
		req.add(new SkillRequirement(Skill.AGILITY, 15, false));
		req.add(new SkillRequirement(Skill.COOKING, 30, false));
		req.add(new SkillRequirement(Skill.FISHING, 5, false));
		req.add(new ItemRequirement("65 Fishing for Raw Karambwan if any type of Ironman account.", -1, -1));
		return req;
	}

	@Override
	public QuestPointReward getQuestPointReward()
	{
		return new QuestPointReward(2);
	}

	@Override
	public List<ExperienceReward> getExperienceRewards()
	{
		return Arrays.asList(
				new ExperienceReward(Skill.COOKING, 5000),
				new ExperienceReward(Skill.FISHING, 5000),
				new ExperienceReward(Skill.ATTACK, 2500),
				new ExperienceReward(Skill.STRENGTH, 2500));
	}

	@Override
	public List<ItemReward> getItemRewards()
	{
		return Collections.singletonList(new ItemReward("Coins", ItemID.COINS, 2000));
	}

	@Override
	public List<UnlockReward> getUnlockRewards()
	{
		return Arrays.asList(
				new UnlockReward("Ability to catch and cook Karambwans (after talking to Tinsay after the quest)"),
				new UnlockReward("Ability to use Tai Bwo Wannai teleport scrolls"),
				new UnlockReward("Ability to complete the smithing section of Barbarian Training"));
	}

	@Override
	public ArrayList<PanelDetails> getPanels()
	{
		ArrayList<PanelDetails> allSteps = new ArrayList<>();
		allSteps.add(new PanelDetails("Starting Off", Collections.singletonList(talkToTimfrakuStart), hammer,
			smallFishingNet, pestleAndMortar, spear, agilityPotion4, rangedOrMagic, tinderbox, slicedBananaOrKnife, logsForFire));
		allSteps.add(new PanelDetails("Gathering quest materials", Arrays.asList(fishKarambwaji, goToLubufu,
			getMoreVessel, fillVessel, getRum, sliceBanana, makeBananaRum), smallFishingNet, slicedBananaOrKnife));
		allSteps.add(new PanelDetails("Helping Tiadechel", talkToTiadeche1, giveVessel, askAboutResearch));
		allSteps.add(new PanelDetails("Collecting final items", Arrays.asList(pickupSeaweed, getJogreBones,
			burnBones, pickupBurntBones, makeKarambwanjiPaste, usePasteOnBones, getPoisonKarambwan, cookBones, cookKarambwan,
			usePestleOnKarambwan, usePasteOnSpear), tinderbox, karambwanji, pestleAndMortar));
		allSteps.add(new PanelDetails("Helping Tamayu and Tinsay", Arrays.asList(talkToTamayu1, givePotion, giveSpear,
			goOnHuntToKill, getMonkeyCorpse, useCorpseOnTamayu, makeSeaweedSandwich, talkToTinsay, useVesselOnTinsay, goToTiadecheFinal)
			, agilityPotion4, poisonedSpear, karamjanRumWithBanana, marinatedJogreBones));
		allSteps.add(new PanelDetails("Finishing the quest", goToTimfrakuLadderEnd, talkToTimfrakuEnd));
		return allSteps;
	}
}
