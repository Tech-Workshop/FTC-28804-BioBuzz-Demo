package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Config
public class Constants {
	/*
	 * General Constants
	 */
	public static final class General {
		//Debug mode
		public static final boolean DEBUG_MODE=false;

		//Controller
		public static final double CONTROLLER_DEADBAND=0.05;
	}

	/*
	 * Autonomous Constants
	 */
	public static final class Auton {
	}

	/*
	 * Sensor Constants
	 */
	public static final class Sensors {
		//Pinpoint Odometry Computer
		public static final String ODOMETRY_ID = "pinpoint";
	}

	/*
	 * Drivetrain Constants
	 */
	public static final class Drive {
		//Motor device IDs
		public static final String DRIVE_LEFT_FRONT_ID = "drive_left_front";
		public static final String DRIVE_LEFT_BACK_ID = "drive_left_back";
		public static final String DRIVE_RIGHT_FRONT_ID = "drive_right_front";
		public static final String DRIVE_RIGHT_BACK_ID = "drive_right_back";

		//Direction
		public static final DcMotorSimple.Direction DRIVE_LEFT_FRONT_DIRECTION = DcMotorSimple.Direction.FORWARD;
		public static final DcMotorSimple.Direction DRIVE_LEFT_BACK_DIRECTION = DcMotorSimple.Direction.REVERSE;
		public static final DcMotorSimple.Direction DRIVE_RIGHT_FRONT_DIRECTION = DcMotorSimple.Direction.FORWARD;
		public static final DcMotorSimple.Direction DRIVE_RIGHT_BACK_DIRECTION = DcMotorSimple.Direction.FORWARD;

		//Maximum speed multiplier (0-1.0)
		public static final double DRIVE_SPEED = 0.40;
		public static final double TURN_POWER_MULTIPLIER = 1.0;
		public static final double TURN_POWER_EXTENDED = 0.45;

		//Teleop Heading Correction
		public static final double TURN_POWER_CLOSE = 1.0; //Power multiplier when within 1 radian (ORIG=1.0)
		public static final double TURN_POWER_FF = 0.12; //Feedforward drive power when correcting small heading errors (ORIG=0.03)
		public static final double TURN_ERROR = 0.06; //Allowed auto-turn error (teleop only)

		//Strafing multiplier
		public static final double STRAFE_MULTIPLIER = 1.2;
	}

	/*
	 * Intake Constants
	 */
	public static final class Intake {
		public static final String INTAKE_ID = "intake";
		public static final DcMotorSimple.Direction INTAKE_DIRECTION = DcMotorSimple.Direction.REVERSE;

		public static final String INTAKE_RETRACTOR_LEFT = "intake_retractor_left"; //Axon Max
		public static final String INTAKE_RETRACTOR_RIGHT = "intake_retractor_right"; //Axon Max

		public static final double INTAKE_RETRACTOR_RETRACTED_POS=0.24;
		public static final double INTAKE_RETRACTOR_EXTENDED_POS=0.64;
	}

	/*
	 * Shooter Constants
	 */
	public static final class Shooter {
		public static final String SHOOTER_ID = "shooter";
		public static final DcMotorSimple.Direction SHOOTER_DIRECTION = DcMotorSimple.Direction.REVERSE;

		public static final String BALL_STOP_ID = "shooter_angle"; //Axon Max
		public static final double BALL_STOP_DOWN=0.68; //Minimum down position
		public static final double BALL_STOP_UP=0.14; //Maximum up position
	}

	/*
	 * Indexer Constants
	 */
	public static final class Indexer {
		public static final String INDEXER_LEFT_ID = "indexer_left"; //Axon Max (CR Mode)
		public static final String INDEXER_RIGHT_ID = "indexer_right"; //Axon Max (CR Mode)
	}

}
