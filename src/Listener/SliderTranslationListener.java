package Listener;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import helper.Testat;

public class SliderTranslationListener implements ChangeListener{
	private Testat target;
	
	public SliderTranslationListener(Testat target)
	{
		this.target = target;
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		JSlider source = (JSlider)e.getSource();
        target.updateTranslation(source.getName(), source.getValue()/(float)100);
    }
}
