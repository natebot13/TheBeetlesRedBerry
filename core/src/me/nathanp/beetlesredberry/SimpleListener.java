package me.nathanp.beetlesredberry;

import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Mainline;
import com.brashmonkey.spriter.Player;

public class SimpleListener implements Player.PlayerListener {

    @Override
    public void animationFinished(Animation animation) {}

    @Override
    public void animationChanged(Animation oldAnim, Animation newAnim) {}

    @Override
    public void preProcess(Player player) {}

    @Override
    public void postProcess(Player player) {}

    @Override
    public void mainlineKeyChanged(Mainline.Key prevKey, Mainline.Key newKey) {}
    
}
