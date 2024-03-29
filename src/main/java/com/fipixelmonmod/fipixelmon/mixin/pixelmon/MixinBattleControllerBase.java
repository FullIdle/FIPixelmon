package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.common.api.battle.participants.BCParticipant;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;

@Mixin(BattleControllerBase.class)
public abstract class MixinBattleControllerBase{
    @Shadow public ArrayList<BattleParticipant> participants;

    @Shadow public abstract void updateRemovedPokemon(PixelmonWrapper poke);

    @Shadow private List<PixelmonWrapper> switchingOut;

    /**
     * @author
     * @reason
     */
    @Overwrite
    void checkFaint() {
        List<PixelmonWrapper> fainted = new ArrayList<>();
        for (BattleParticipant p : this.participants) {
            List<PixelmonWrapper> toRemove = new ArrayList<>();
            for (PixelmonWrapper pw : p.controlledPokemon) {
                if (pw.isFainted() && !pw.isSwitching) {
                    if (pw.removePrimaryStatus() != null)
                        pw.sendStatusPacket(-1);
                    pw.update(EnumUpdateType.Status);
                    p.updatePokemon(pw);
                    if (p.addSwitchingOut(pw)) {
                        fainted.add(pw);
                        continue;
                    }
                    toRemove.add(pw);
                }
            }
            for (PixelmonWrapper pw : toRemove) {
                pw.isSwitching = false;
                pw.wait = false;
                p.controlledPokemon.remove(pw);
                updateRemovedPokemon(pw);
            }
        }
        this.switchingOut.addAll(fainted);
        for (PixelmonWrapper pw : fainted) {
            BattleParticipant p = pw.getParticipant();
            p.faintedLastTurn = true;
            pw.willTryFlee = false;
            p.wait = true;
            p.getNextPokemon(pw.battlePosition);
        }
    }
}
