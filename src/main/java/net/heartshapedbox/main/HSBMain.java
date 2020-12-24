package net.heartshapedbox.main;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.heartshapedbox.HSBMiscLogic;
import net.heartshapedbox.body.BodyPartProvider;
import net.heartshapedbox.body.impl.FootBodyPart;
import net.heartshapedbox.body.impl.LegBodyPart;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Pair;

import static net.minecraft.server.command.CommandManager.literal;

public class HSBMain implements ModInitializer {
	@Override
	public void onInitialize() {
		ServerTickEvents.END_SERVER_TICK.register( minecraftServer ->
			// Debuff all players accordingly
			PlayerStream.all(minecraftServer).forEach(HSBMiscLogic::debuffPlayer)
		);
		
		// Debug command
		// TODO: REMOVE THIS! or add an op requirement idc
		CommandRegistrationCallback.EVENT.register((commandDispatcher, b) ->
			commandDispatcher.register(
			literal("hsb").executes(context -> {
				ServerCommandSource source = context.getSource();
				BodyPartProvider provider = (BodyPartProvider)context.getSource().getPlayer();
				
				source.sendFeedback(new LiteralText("Legs"), false);
				Pair<LegBodyPart, LegBodyPart> legs = provider.getLegs();
				source.sendFeedback(new LiteralText("- LEFT: " + legs.getLeft().getHealth()), false);
				source.sendFeedback(new LiteralText("- RIGHT: " + legs.getRight().getHealth()), false);
				
				source.sendFeedback(new LiteralText("Feet"), false);
				Pair<FootBodyPart, FootBodyPart> feet = provider.getFeet();
				source.sendFeedback(new LiteralText("- LEFT: " + feet.getLeft().getHealth()), false);
				source.sendFeedback(new LiteralText("- RIGHT: " + feet.getRight().getHealth()), false);
				return 1;
			})
				.then(literal("reset").executes(context -> {
					ServerCommandSource source = context.getSource();
					BodyPartProvider provider = (BodyPartProvider)context.getSource().getPlayer();
					
					Pair<LegBodyPart, LegBodyPart> legs = provider.getLegs();
					legs.getLeft().setHealth(legs.getLeft().getMaxHealth());
					legs.getRight().setHealth(legs.getRight().getMaxHealth());
					
					Pair<FootBodyPart, FootBodyPart> feet = provider.getFeet();
					feet.getLeft().setHealth(feet.getLeft().getMaxHealth());
					feet.getRight().setHealth(feet.getRight().getMaxHealth());
					
					source.sendFeedback(new LiteralText("Reset health"), false);
					return 1;
				}))
		));
	}
}
