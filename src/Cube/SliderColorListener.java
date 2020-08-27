package Cube;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SliderColorListener implements ChangeListener {

	private Cube target;
	
	SliderColorListener(Cube target)
	{
		this.target = target;
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {
            float val = (source.getValue())/(float)source.getMaximum();
            target.updateColors(source.getName(), val);
        }
        
	}
}
