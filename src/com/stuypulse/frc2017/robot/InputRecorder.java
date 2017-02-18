package com.stuypulse.frc2017.robot;
import java.util.ArrayList;
import com.stuypulse.frc2017.util.Gamepad;
import java.util.List;



/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class InputRecorder{

	public Gamepad driverPad;
	public Gamepad operatorPad;
	private double duration;
	private double time;
	/*/IMPORTANT:
	
	*This method has a duration period of when it would record someone's operator and driverpad inputs. The first time it starts recording, it assumes all buttons arent
	*pressed. If a button is held to the very end of the recording duration, it would automatically record that the button has been released at the end of the duration.
	*I did not review this code well, so expect a lot of bugs.
	*
	*
	*
	*/
	public InputRecorder(double duration) {
		driverPad = new Gamepad(RobotMap.DRIVER_PAD_PORT);
		operatorPad = new Gamepad(RobotMap.OPERATOR_PAD_PORT);
		this.duration = duration;
		this.time = duration + System.currentTimeMillis();
		ArrayList<ArrayList<Boolean>> InputArray = new ArrayList<ArrayList<Boolean>>();
		ArrayList<ArrayList<Double>> InputTimesArray = new ArrayList<ArrayList<Double>>();//0-17 for input types, Double to check each one's time.
		Loop(InputArray, InputTimesArray);
	}
	public void Loop( ArrayList<ArrayList<Boolean>>  InputArray,ArrayList<ArrayList<Double>> InputTimesArray){
		reset(InputArray);
		int k = 0;
		while(System.currentTimeMillis() < time){
			update(InputArray);
			while (k <= 17){
				UpdateInputTime(k, InputArray,InputTimesArray);
				k++;
			}
		};
		CutEndpoint(InputTimesArray);
	}
	public void UpdateInputTime(int k,ArrayList<ArrayList<Boolean>>  InputArray,ArrayList<ArrayList<Double>> InputTimesArray){
		if(InputArray.get(1).get(k) != (InputArray.get(0)).get(k)){
			InputTimesArray.get(k).add(duration - time +System.currentTimeMillis());
		}
	}
	public void CutEndpoint(ArrayList<ArrayList<Double>> InputTimesArray){
		int g = 0;
		while (g <= 17){
			if(InputTimesArray.get(g).size()%2 == 0){
				InputTimesArray.get(g).add(duration);
			}
			g++;
		}
	}
	public void reset( ArrayList<ArrayList<Boolean>> InputArray){
		int n = 0;
		while (n <= 17){
			InputArray.get(0).set(n,false);
			InputArray.get(1).set(n,false);
			n++;
		}
	}
	public void update( ArrayList<ArrayList<Boolean>> InputArray){
		for (int i = 0; i <= 17; i++){
			InputArray.get(0).set(i,InputArray.get(1).get(i));
		}
		InputArray.get(1).set(0,operatorPad.getRawLeftButton());
		InputArray.get(1).set(1,operatorPad.getRawRightButton());
		InputArray.get(1).set(2,operatorPad.getRawTopButton());
		InputArray.get(1).set(3 , operatorPad.getRawBottomButton());
		InputArray.get(1).set(4 , operatorPad.getRawDPadUp());
		InputArray.get(1).set(5 , operatorPad.getRawDPadDown());
		InputArray.get(1).set(6 , operatorPad.getRawDPadLeft());
		InputArray.get(1).set(7 , operatorPad.getRawDPadRight());
		InputArray.get(1).set(8 , operatorPad.getRawLeftBumper());
		InputArray.get(1).set(9 , operatorPad.getRawRightBumper());
		InputArray.get(1).set(10 , operatorPad.getRawLeftTrigger());
		InputArray.get(1).set(11 , operatorPad.getRawRightTrigger());
		InputArray.get(1).set(12 , operatorPad.getRawLeftAnalogButton());
		InputArray.get(1).set(13 , operatorPad.getRawRightAnalogButton());
		
		InputArray.get(1).set(14 , driverPad.getRawLeftBumper());
		InputArray.get(1).set(15 , driverPad.getRawRightBumper());
		InputArray.get(1).set(16 , driverPad.getRawLeftTrigger());
		InputArray.get(1).set(17 , driverPad.getRawRightTrigger());
	}

}


