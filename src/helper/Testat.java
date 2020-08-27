package helper;

public interface Testat {

	public void updateColors(String Slidername, float value);
	public void updateCoords(String Slidername, float value);
	public void updateRotation(String name, int value);
	public void updateScale(float f);
	public void updateTranslation(String name, float f);
	public void updatePerspective(String name, float val);
	public void updateBlendFunction(String name, int function);
	
}
