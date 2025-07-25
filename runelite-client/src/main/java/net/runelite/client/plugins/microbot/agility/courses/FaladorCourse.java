package net.runelite.client.plugins.microbot.agility.courses;

import java.util.List;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.gameval.ObjectID;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.agility.models.AgilityObstacleModel;
import net.runelite.client.plugins.microbot.util.Global;
import net.runelite.client.plugins.microbot.util.misc.Operation;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;

public class FaladorCourse implements AgilityCourseHandler
{
	@Override
	public WorldPoint getStartPoint()
	{
		return new WorldPoint(3036, 3341, 0);
	}

	@Override
	public List<AgilityObstacleModel> getObstacles()
	{
		return List.of(
			new AgilityObstacleModel(ObjectID.ROOFTOPS_FALADOR_WALLCLIMB),
			new AgilityObstacleModel(ObjectID.ROOFTOPS_FALADOR_TIGHTROPE_1),
			new AgilityObstacleModel(ObjectID.ROOFTOPS_FALADOR_HANDHOLDS_START),
			new AgilityObstacleModel(ObjectID.ROOFTOPS_FALADOR_GAP_1, -1, 3358, Operation.GREATER, Operation.LESS_EQUAL),
			new AgilityObstacleModel(ObjectID.ROOFTOPS_FALADOR_GAP_2, 3041, 3361, Operation.GREATER, Operation.GREATER_EQUAL),
			new AgilityObstacleModel(ObjectID.ROOFTOPS_FALADOR_TIGHTROPE_2),
			new AgilityObstacleModel(ObjectID.ROOFTOPS_FALADOR_TIGHTROPE_3),
			new AgilityObstacleModel(ObjectID.ROOFTOPS_FALADOR_GAP_3, -1, 3353, Operation.GREATER, Operation.GREATER_EQUAL),
			new AgilityObstacleModel(ObjectID.ROOFTOPS_FALADOR_LEDGE_1, 3016, -1, Operation.GREATER_EQUAL, Operation.GREATER),
			new AgilityObstacleModel(ObjectID.ROOFTOPS_FALADOR_LEDGE_2, -1, 3343, Operation.GREATER, Operation.GREATER_EQUAL),
			new AgilityObstacleModel(ObjectID.ROOFTOPS_FALADOR_LEDGE_3A, -1, 3335, Operation.GREATER, Operation.GREATER_EQUAL),
			new AgilityObstacleModel(ObjectID.ROOFTOPS_FALADOR_LEDGE_4, 3017, -1, Operation.LESS, Operation.GREATER),
			new AgilityObstacleModel(ObjectID.ROOFTOPS_FALADOR_EDGE)
		);
	}

	@Override
	public Integer getRequiredLevel()
	{
		return 50;
	}

	@Override
	public boolean waitForCompletion(final int agilityExp, final int plane)
	{
		double initialHealth = Rs2Player.getHealthPercentage();
		int timeoutMs = 15000;

		Global.sleepUntil(() -> Microbot.getClient().getSkillExperience(Skill.AGILITY) != agilityExp || Rs2Player.getHealthPercentage() < initialHealth, timeoutMs);

		boolean gainedExp = Microbot.getClient().getSkillExperience(Skill.AGILITY) != agilityExp;
		boolean lostHealth = Rs2Player.getHealthPercentage() < initialHealth;

		return gainedExp || lostHealth;
	}
}
