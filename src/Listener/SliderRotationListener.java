package Listener;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import helper.Testat;

public class SliderRotationListener implements ChangeListener{
	private Testat target;
	
	public SliderRotationListener(Testat target)
	{
		this.target = target;
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		JSlider source = (JSlider)e.getSource();
        target.updateRotation(source.getName(), source.getValue());
    }
}
