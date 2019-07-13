package flaxbeard.cyberware.client.gui;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import flaxbeard.cyberware.common.block.tile.TileEntityBlueprintArchive;

public class ContainerBlueprintArchive extends Container
{
	private TileEntityBlueprintArchive archive;
	private int numRows;

	public ContainerBlueprintArchive(IInventory playerInventory, TileEntityBlueprintArchive archive)
	{
		this.archive = archive;
		this.numRows = archive.slots.getSlots() / 9;
		int i = (numRows - 4) * 18;

		for (int j = 0; j < numRows; ++j)
		{
			for (int k = 0; k < 9; ++k)
			{
				addSlotToContainer(new SlotItemHandler(archive.slots, k + j * 9, 8 + k * 18, 18 + j * 18));
			}
		}

		for (int l = 0; l < 3; ++l)
		{
			for (int j1 = 0; j1 < 9; ++j1)
			{
				addSlotToContainer(new Slot(playerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
			}
		}

		for (int i1 = 0; i1 < 9; ++i1)
		{
			addSlotToContainer(new Slot(playerInventory, i1, 8 + i1 * 18, 161 + i));
		}
	}

	public boolean canInteractWith(EntityPlayer entityPlayer)
	{
		return archive.isUseableByPlayer(entityPlayer);
	}

	@Nonnull
	public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index < numRows * 9)
			{
				if (!mergeItemStack(itemstack1, numRows * 9, inventorySlots.size(), true))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (!mergeItemStack(itemstack1, 0, numRows * 9, false))
			{
				return ItemStack.EMPTY;
			}

			if (itemstack1.getCount() == 0)
			{
				slot.putStack(ItemStack.EMPTY);
			}
			else
			{
				slot.onSlotChanged();
			}
		}

		return itemstack;
	}

}