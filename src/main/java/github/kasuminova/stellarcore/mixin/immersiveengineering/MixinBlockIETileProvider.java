package github.kasuminova.stellarcore.mixin.immersiveengineering;

import blusunrize.immersiveengineering.common.blocks.BlockIETileProvider;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import com.llamalad7.mixinextras.sugar.Local;
import github.kasuminova.stellarcore.common.config.StellarCoreConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collections;
import java.util.Iterator;

@Mixin(BlockIETileProvider.class)
@SuppressWarnings("MethodMayBeStatic")
public class MixinBlockIETileProvider {

    @Redirect(
            method = "getDrops",
            at = @At(value = "INVOKE", target = "Ljava/util/Iterator;hasNext()Z", remap = false),
            remap = false
    )
    @SuppressWarnings("rawtypes")
    private boolean onGetDrops(final Iterator instance, @Local(name = "tile") TileEntity tile) {
        if (instance.hasNext()) {
            return true;
        }
        if (!StellarCoreConfig.BUG_FIXES.immersiveEngineering.blockIEMultiblock) {
            return false;
        }
        if (tile instanceof IIEInventory ieInv) {
            NonNullList<ItemStack> inventory = ieInv.getInventory();
            if (inventory == null || inventory.isEmpty()) {
                return false;
            }
            Collections.fill(inventory, ItemStack.EMPTY);
        }
        return false;
    }

}
