package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.event.block.BlockRedstoneEvent; // CraftBukkit

public class BlockRedstoneTorch extends BlockTorch {

    private final boolean isOn;

    protected BlockRedstoneTorch(boolean flag) {
        this.isOn = flag;
        this.a(true);
        this.a((CreativeModeTab) null);
    }

    public int a(World world) {
        return 2;
    }

    public void applyPhysics(World world, BlockPosition blockposition) {
        if (this.isOn) {
            world.applyPhysics(blockposition.shift(EnumDirection.UP), this);
        }
    }

    public void onPlace(World world, BlockPosition blockposition, IBlockData iblockdata) {
        applyPhysics(world, blockposition);

    }

    public void remove(World world, BlockPosition blockposition, IBlockData iblockdata) {
        applyPhysics(world, blockposition);

    }

    public int a(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, EnumDirection enumdirection) {
        return this.isOn && iblockdata.get(BlockRedstoneTorch.FACING) != enumdirection ? 15 : 0;
    }

    private boolean g(World world, BlockPosition blockposition, IBlockData iblockdata) {
        EnumDirection enumdirection = ((EnumDirection) iblockdata.get(BlockRedstoneTorch.FACING)).opposite();

        return world.isBlockFacePowered(blockposition.shift(enumdirection), enumdirection);
    }

    public void a(World world, BlockPosition blockposition, IBlockData iblockdata, Random random) {}

    public void b(World world, BlockPosition blockposition, IBlockData iblockdata, Random random) {
        boolean flag = this.g(world, blockposition, iblockdata);

        if (this.isOn != flag) {
            return;
        }

        // CraftBukkit start
        org.bukkit.plugin.PluginManager manager = world.getServer().getPluginManager();
        org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
        int oldCurrent = this.isOn ? 15 : 0;

        BlockRedstoneEvent event = new BlockRedstoneEvent(block, oldCurrent, oldCurrent);
        // CraftBukkit end

        if (this.isOn) {
                // CraftBukkit start
                event.setNewCurrent(0);
                manager.callEvent(event);
                if (event.getNewCurrent() != 0) {
                    return;
                }
                // CraftBukkit end
                world.setTypeAndData(blockposition, Blocks.UNLIT_REDSTONE_TORCH.getBlockData().set(BlockRedstoneTorch.FACING, iblockdata.get(BlockRedstoneTorch.FACING)), 3);
        } else {
            // CraftBukkit start
            if (oldCurrent != 15) {
                event.setNewCurrent(15);
                manager.callEvent(event);
                if (event.getNewCurrent() != 15) {
                    return;
                }
            }
            // CraftBukkit end
            world.setTypeAndData(blockposition, Blocks.REDSTONE_TORCH.getBlockData().set(BlockRedstoneTorch.FACING, iblockdata.get(BlockRedstoneTorch.FACING)), 3);
        }

    }

    public void doPhysics(World world, BlockPosition blockposition, IBlockData iblockdata, Block block) {
        if (!this.e(world, blockposition, iblockdata) && this.isOn == this.g(world, blockposition, iblockdata)) {
            world.a(blockposition, (Block) this, this.a(world));
        }
    }

    public int b(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, EnumDirection enumdirection) {
        return enumdirection == EnumDirection.DOWN ? this.a(iblockaccess, blockposition, iblockdata, enumdirection) : 0;
    }

    public Item getDropType(IBlockData iblockdata, Random random, int i) {
        return Item.getItemOf(Blocks.REDSTONE_TORCH);
    }

    public boolean isPowerSource() {
        return true;
    }

    public boolean b(Block block) {
        return block == Blocks.UNLIT_REDSTONE_TORCH || block == Blocks.REDSTONE_TORCH;
    }

    static class RedstoneUpdateInfo {

        BlockPosition a;
        long b;

        public RedstoneUpdateInfo(BlockPosition blockposition, long i) {
            this.a = blockposition;
            this.b = i;
        }
    }
}
