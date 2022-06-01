package owmii.powah.block.reactor;

import owmii.lib.util.NBT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class Builder {
    private final ReactorTile reactor;
    private List<BlockPos> queue = new ArrayList<>();
    public boolean built;

    private int delay = 5;

    public Builder(ReactorTile reactor) {
        this.reactor = reactor;
    }


    public void read(CompoundTag nbt) {
        this.built = nbt.getBoolean("built");
        if (!this.built) {
            this.queue = NBT.readPosList(nbt, "queue_pos", new ArrayList<>());
        }
    }

    public void write(CompoundTag nbt) {
        nbt.putBoolean("built", this.built);
        if (!this.built) {
            NBT.writePosList(nbt, this.queue, "queue_pos");
        }
    }

    public boolean isDone(Level world) {
        if (this.built) return true;
        else if (!this.queue.isEmpty()) {
            if (this.delay-- <= 0) {
                Iterator<BlockPos> itr = this.queue.iterator();
                while (itr.hasNext()) {
                    BlockPos pos = itr.next();
                    BlockState state = this.reactor.getBlock().defaultBlockState();
                    world.setBlock(pos, state.setValue(ReactorBlock.CORE, false), 3);
                    BlockEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity instanceof ReactorPartTile) {
                        ReactorPartTile part = (ReactorPartTile) tileEntity;
                        part.setCorePos(this.reactor.getBlockPos());
                        world.levelEvent(2001, pos, Block.getId(this.reactor.getBlockState()));
                        itr.remove();
                        this.delay = 5;
                        return false;
                    }
                }
            }
        } else {
            for (Direction side : Direction.values()) {
                if (side.equals(Direction.DOWN)) continue;
                BlockPos pos = this.reactor.getBlockPos().relative(side).above(side.equals(Direction.UP) ? 2 : 0);
                BlockEntity tileEntity = world.getBlockEntity(pos);
                if (tileEntity instanceof ReactorPartTile) {
                    ReactorPartTile part = (ReactorPartTile) tileEntity;
                    part.setExtractor(true);
                }
            }
            for (BlockPos pos : getPosList()) {
                BlockEntity tileEntity = world.getBlockEntity(pos);
                if (tileEntity instanceof ReactorPartTile) {
                    ReactorPartTile part = (ReactorPartTile) tileEntity;
                    part.setBuilt(true);
                    part.sync();
                }
            }
            this.built = true;
            this.reactor.sync();
        }
        return false;
    }

    public void shuffle() {
        this.queue.addAll(getPosList());
        Collections.shuffle(this.queue);
    }

    public void demolish(Level world) {
        List<BlockPos> list = getPosList();
        list.add(this.reactor.getBlockPos());
        int count = 0;
        for (BlockPos blockPos : list) {
            if (world.getBlockState(blockPos).getBlock().equals(this.reactor.getBlock())) {
                count++;
                world.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
            }
        }
        Block.popResource(world, this.reactor.getBlockPos(), new ItemStack(this.reactor.getBlock(), count + this.queue.size()));
        world.setBlock(this.reactor.getBlockPos(), Blocks.AIR.defaultBlockState(), 3);
        this.queue.clear();
    }

    public List<BlockPos> getPosList() {
        final BlockPos pos = this.reactor.getBlockPos();
        return BlockPos.betweenClosedStream(pos.offset(-1, 0, -1), pos.offset(1, 3, 1))
                .map(BlockPos::immutable)
                .filter(pos1 -> !pos1.equals(pos))
                .collect(Collectors.toList());
    }
}
