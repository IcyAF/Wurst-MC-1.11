/*
 * Copyright � 2014 - 2017 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.features.mods;

import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.features.Feature;
import tk.wurst_client.utils.InventoryUtils;

@Mod.Info(
	description = "Turns your bow into a machine gun.\n"
		+ "Tip: This works with BowAimbot.",
	name = "FastBow",
	tags = "RapidFire, BowSpam, fast bow, rapid fire, bow spam",
	help = "Mods/FastBow")
@Mod.Bypasses(ghostMode = false, latestNCP = false, olderNCP = false)
public class FastBowMod extends Mod implements UpdateListener
{
	@Override
	public Feature[] getSeeAlso()
	{
		return new Feature[]{wurst.mods.bowAimbotMod};
	}
	
	@Override
	public void onEnable()
	{
		wurst.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		// check if right-clicking
		if(!mc.gameSettings.keyBindUseItem.pressed)
			return;
		
		// check fly-kick
		if(!mc.player.onGround && !mc.player.capabilities.isCreativeMode)
			return;
		
		// check health
		if(mc.player.getHealth() <= 0)
			return;
		
		// check held item
		ItemStack stack = mc.player.inventory.getCurrentItem();
		if(InventoryUtils.isEmptySlot(stack)
			|| !(stack.getItem() instanceof ItemBow))
			return;
		
		mc.playerController.processRightClick(mc.player, mc.world,
			EnumHand.MAIN_HAND);
		
		for(int i = 0; i < 20; i++)
			mc.player.connection.sendPacket(new CPacketPlayer(false));
		
		mc.playerController.onStoppedUsingItem(mc.player);
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
	}
}
