package flaxbeard.cyberware.api.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface ICyberware
{

	public int installedStackSize(ItemStack stack);
	public ItemStack[][] required(ItemStack stack);
	public boolean isIncompatible(ItemStack stack, ItemStack comparison);
	boolean isEssential(ItemStack stack);
	public List<String> getInfo(ItemStack stack);
	public int getCapacity(ItemStack wareStack);

	public void onAdded(EntityLivingBase entity, ItemStack stack);
	public void onRemoved(EntityLivingBase entity, ItemStack stack);
	public int getEssenceCost(ItemStack stack);
	default String getUnlocalizedOrigin(ItemStack stack)
	{
		return "cyberware.gui.tablet.catalog.sort.other";
	}

	
	/**
	 * Returns a Quality object representing the quality of this stack - all
	 * changes that this Quality has to function must be handled internally,
	 * this is just for the tooltip and external factors. See CyberwareAPI for
	 * the base Qualities.
	 * 
	 * @param stack	The ItemStack to check
	 * @return		An instance of Quality
	 */
	public Quality getQuality(ItemStack stack);
	
	/**
	 * Sets the Quality tag of this ItemStack to the specified Quality and
	 * returns the changed ItemStack. If the ItemStack cannot contain the
	 * Quality, returns the passed ItemStack
	 * 
	 * @param stack		The ItemStack to apply the Quality to
	 * @param quality	The Quality to apply
	 * @return			ItemStack with Quality applied
	 */
	public ItemStack setQuality(ItemStack stack, Quality quality);
	
	/**
	 * Whether or not this piece of Cyberware can handle the given Quality
	 * 
	 * @param stack		The ItemStack to check
	 * @param quality	The Quality to check
	 * @return			Whether this Quality can be applied to this stack
	 */
	public boolean canHoldQuality(ItemStack stack, Quality quality);
	
	/**
	 * Returns an array of EnumSlots representing the areas of the body in which
	 * this piece of Cyberware can be installed. See EnumSlot below.
	 * 
	 * @param stack The ItemStack to check
	 * @return		Array of valid slots
	 */
	default EnumSlot[] getSlots(ItemStack stack)
	{
		return new EnumSlot[] { getSlot(stack) };
	}
	
	default EnumSlot getFirstSlot(ItemStack stack)
	{
		return getSlots(stack)[0];
	}
	
	default boolean canFitInSlot(ItemStack stack, EnumSlot slot)
	{
		for (EnumSlot check : getSlots(stack))
		{
			if (check == slot) return true;
		}
		return false;
	}
	
	@Deprecated
	default EnumSlot getSlot(ItemStack stack)
	{
		return getFirstSlot(stack);
	}

	public class Quality
	{
		private static Map<String, Quality> mapping = new HashMap<String, Quality>();
		public static List<Quality> qualities = new ArrayList<Quality>();
		private String unlocalizedName;
		private String nameModifier;
		private String spriteSuffix;

		public Quality(String unlocalizedName)
		{
			this(unlocalizedName, null, null);
		}
		
		public Quality(String unlocalizedName, String nameModifier, String spriteSuffix)
		{
			this.unlocalizedName = unlocalizedName;
			this.nameModifier = nameModifier;
			this.spriteSuffix = spriteSuffix;
			mapping.put(unlocalizedName, this);
			qualities.add(this);
		}
		
		public String getUnlocalizedName()
		{
			return unlocalizedName;
		}

		public static Quality getQualityFromString(String name)
		{
			if (mapping.containsKey(name))
			{
				return mapping.get(name);
			}
			return null;
		}

		public String getNameModifier()
		{
			return nameModifier;
		}
		
		public String getSpriteSuffix()
		{
			return spriteSuffix;
		}
	}

	public enum EnumSlot
	{
		EYES(12, "eyes"),
		CRANIUM(11, "cranium"),
		HEART(14, "heart"),
		LUNGS(15, "lungs"),
		LOWER_ORGANS(17, "lowerOrgans"),
		SKIN(18, "skin"),
		MUSCLE(19, "muscle"),
		BONE(20, "bone"),
		ARM(21, "armRight"),
		HAND(22, "handRight", false, false),
		LEG(23, "legRight"),
		FOOT(24, "footRight", false, false),
		ARMLEFT(25, "armLeft"),
		HANDLEFT(26, "handLeft", false, false),
		LEGLEFT(27, "legLeft"),
		FOOTLEFT(28, "footLeft", false, false);
		
		private final int slotNumber;
		private final String name;
		private final boolean sidedSlot;
		private final boolean hasEssential;
		
		private EnumSlot(int slot, String name, boolean sidedSlot, boolean hasEssential)
		{
			this.slotNumber = slot;
			this.name = name;
			this.sidedSlot = false;
			this.hasEssential = hasEssential;
		}
		
		private EnumSlot(int slot, String name)
		{
			this(slot, name, false, true);
		}
		
		public int getSlotNumber()
		{
			return slotNumber;
		}
		
		public static EnumSlot getSlotByPage(int page)
		{
			for (EnumSlot slot : values())
			{
				if (slot.getSlotNumber() == page)
				{
					return slot;
				}
			}
			return null;
		}

		public String getUnlocalizedName()
		{
			return "cyberware.slot." + name;
		}
		
		public String getBaseName()
		{
			return name;
		}
		
		public boolean isSided()
		{
			return sidedSlot;
		}

		public boolean hasEssential()
		{
			return hasEssential;
		}
	}

	@Deprecated
	public interface ISidedLimb
	{
		@Deprecated
		public EnumSide getSide(ItemStack stack);
		
		public enum EnumSide
		{
			LEFT,
			RIGHT;
		}
	}

}
