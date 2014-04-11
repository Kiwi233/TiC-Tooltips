package squeek.tictooltips.helpers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import tconstruct.common.TContent;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.tools.ToolCore;
import tconstruct.library.tools.ToolMaterial;

public class ToolHelper
{

	public static boolean hasToolTag(ItemStack itemStack)
	{
		return itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("InfiTool");
	}

	public static NBTTagCompound getToolTag(ItemStack tool)
	{
		NBTTagCompound tag;
		tag = tool.getTagCompound().getCompoundTag("InfiTool");
		return tag;
	}

	public static ToolMaterial getHeadMaterial(NBTTagCompound toolTag)
	{
		ToolMaterial mat = null;
		int matID = toolTag.getInteger("Head");
		if (matID >= 0)
		{
			mat = TConstructRegistry.getMaterial(matID);
		}
		return mat;
	}

	public static boolean hasToolCategory(ToolCore tool, String searchCategory)
	{
		for (String category : tool.toolCategories())
		{
			if (category.equals(searchCategory))
				return true;
		}
		return false;
	}

	public static boolean isWeaponTool(ToolCore tool)
	{
		return hasToolCategory(tool, "weapon");
	}

	public static boolean isBowTool(ToolCore tool)
	{
		return hasToolCategory(tool, "bow");
	}

	public static boolean isAmmoTool(ToolCore tool)
	{
		return hasToolCategory(tool, "ammo");
	}

	public static boolean isDualHarvestTool(ToolCore tool)
	{
		return hasToolCategory(tool, "dualharvest");
	}

	public static boolean isHarvestTool(ToolCore tool)
	{
		return hasToolCategory(tool, "harvest");
	}

	public static boolean isUtilityTool(ToolCore tool)
	{
		return hasToolCategory(tool, "utility");
	}

	public static int getUsedDurability(NBTTagCompound toolTag)
	{
		return toolTag.getInteger("Damage");
	}

	public static int getMaxDurability(NBTTagCompound toolTag)
	{
		return toolTag.getInteger("TotalDurability");
	}

	public static int getEffectiveDurability(NBTTagCompound toolTag)
	{
		return (int) (toolTag.getInteger("TotalDurability") * (1f + getReinforcedLevel(toolTag) * .1f));
	}

	public static int getReinforcedLevel(NBTTagCompound toolTag)
	{
		return toolTag.getInteger("Unbreaking");
	}

	public static float getStonebound(NBTTagCompound toolTag)
	{
		return toolTag.getFloat("Shoddy");
	}

	public static int getRawDamage(ToolCore tool, NBTTagCompound toolTag)
	{
		int rawDamage = toolTag.getInteger("Attack") - tool.getDamageVsEntity(null);
		return rawDamage;
	}

	public static int getDamage(ToolCore tool, NBTTagCompound toolTag)
	{
		int attack = toolTag.getInteger("Attack")+1;
		attack += getShoddinessDamageBonus(toolTag);
		attack *= tool.getDamageModifier();
		if (attack < 1)
			attack = 1;

		return attack;
	}
	
	public static int[] getSmiteDamageRange(ToolCore tool, NBTTagCompound toolTag)
	{
		int staticBonus = 0;
		int variableBonus = 0;
		// TODO: Better way to determine if the tool is of a certain type
		if (tool == TContent.hammer)
        {
            int level = 2;
            staticBonus += level * 2;
            variableBonus += level * 2 + 1;
        }
		if (toolTag.hasKey("ModSmite"))
        {
            int[] array = toolTag.getIntArray("ModSmite");
            int base = array[0] / 18;
            staticBonus += 1 + base;
            variableBonus += base + 1;
        }
		return new int[] {staticBonus, staticBonus+variableBonus};
	}
	
	public static int[] getAntiSpiderDamageRange(ToolCore tool, NBTTagCompound toolTag)
	{
		int staticBonus = 0;
		int variableBonus = 0;
		if (toolTag.hasKey("ModAntiSpider"))
        {
            int[] array = toolTag.getIntArray("ModAntiSpider");
            int base = array[0] / 2;
            staticBonus += 1 + base;
            variableBonus += base + 1;
        }
		return new int[] {staticBonus, staticBonus+variableBonus};
	}
	
	public static int getBurnDuration(ToolCore tool, NBTTagCompound toolTag)
	{
		int burnDuration = 0;
		if (toolTag.hasKey("Fiery"))
        {
			burnDuration += toolTag.getInteger("Fiery") / 5 + 1;
        }
        if (toolTag.getBoolean("Lava"))
        {
        	burnDuration += 3;
        }
		return burnDuration;
	}
	
	public static float getChanceToBehead(ToolCore tool, NBTTagCompound toolTag)
	{
		float chanceToBehead = toolTag.getInteger("Beheading");
		if (tool == TContent.cleaver)
			chanceToBehead += 2;
		chanceToBehead = chanceToBehead / 10;
        return Math.min(1, chanceToBehead);
	}

	public static float getShoddinessDamageBonus(NBTTagCompound toolTag)
	{
		return (float) Math.log(getUsedDurability(toolTag) / 72f + 1) * -2 * getStonebound(toolTag);
	}

	public static float getMaxShoddinessDamageBonus(NBTTagCompound toolTag)
	{
		return (float) Math.log(getMaxDurability(toolTag) / 72f + 1) * -2 * getStonebound(toolTag);
	}

	public static float getShoddinessSpeedBonus(NBTTagCompound toolTag)
	{
		return (float) Math.log(getUsedDurability(toolTag) / 72f + 1) * 2 * getStonebound(toolTag);
	}

	public static float getMaxShoddinessSpeedBonus(NBTTagCompound toolTag)
	{
		return (float) Math.log(getMaxDurability(toolTag) / 72f + 1) * 2 * getStonebound(toolTag);
	}

	public static int getDrawSpeed(NBTTagCompound toolTag)
	{
		return toolTag.getInteger("DrawSpeed");
	}

	public static float getArrowSpeedModifier(NBTTagCompound toolTag)
	{
		return toolTag.getFloat("FlightSpeed");
	}

	public static int getAmmoDamage(NBTTagCompound toolTag)
	{
		return toolTag.getInteger("Attack");
	}

	public static float getWeight(NBTTagCompound toolTag)
	{
		return toolTag.getFloat("Mass");
	}

	public static float getAccuracy(NBTTagCompound toolTag)
	{
		return toolTag.getFloat("Accuracy");
	}

	public static int getPrimaryMiningSpeed(NBTTagCompound toolTag)
	{
		return toolTag.getInteger("MiningSpeed");
	}

	public static int getSecondaryMiningSpeed(NBTTagCompound toolTag)
	{
		return toolTag.getInteger("MiningSpeed2");
	}

	public static int getTotalMiningSpeed(NBTTagCompound toolTag)
	{
		int mineSpeed = toolTag.getInteger("MiningSpeed");
		int heads = 1;

		if (toolTag.hasKey("MiningSpeed2"))
		{
			mineSpeed += toolTag.getInteger("MiningSpeed2");
			heads++;
		}

		if (toolTag.hasKey("MiningSpeedHandle"))
		{
			mineSpeed += toolTag.getInteger("MiningSpeedHandle");
			heads++;
		}

		if (toolTag.hasKey("MiningSpeedExtra"))
		{
			mineSpeed += toolTag.getInteger("MiningSpeedExtra");
			heads++;
		}

		return (int) ((float) mineSpeed / heads);
	}

	public static int getPrimaryHarvestLevel(NBTTagCompound toolTag)
	{
		return toolTag.getInteger("HarvestLevel");
	}

	public static int getSecondaryHarvestLevel(NBTTagCompound toolTag)
	{
		return toolTag.getInteger("HarvestLevel2");
	}

}
