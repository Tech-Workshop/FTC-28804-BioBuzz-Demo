package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Constants;

/*
@TeleOp(group = "UTILITY",name="Utility: Shooter kV")
public class UtilityShooterTunerKv extends OpMode {

	private DcMotorEx shooter;
	double[] increments = { 0.000001, 0.00001, 0.0001, 0.001, 0.01 };
	int incrementIndex = 4;
	double kS=0.09;
	double kV=0.0;
	double goalVelocity=1730;

	@Override
	public void init() {
		shooter = hardwareMap.get(DcMotorEx.class, Constants.Shooter.SHOOTER_ID);
		shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		shooter.setDirection(Constants.Shooter.SHOOTER_DIRECTION);
	}

	@Override
	public void loop() {
		if(gamepad1.dpadRightWasPressed() && incrementIndex<4) {
			incrementIndex++;
		} else if(gamepad1.dpadLeftWasPressed() && incrementIndex>0) {
			incrementIndex--;
		}

		double currentStep=increments[incrementIndex];

		if(gamepad1.dpadUpWasPressed()) { kV += currentStep; }
		if(gamepad1.dpadDownWasPressed()) { kV -= currentStep; }

		double power=(kV*goalVelocity)+kS;
		shooter.setPower(power);

		telemetry.addData("kV","%.6f",kV);
		telemetry.addData("Power","%.3f",power);
		telemetry.addData("Flywheel Goal",goalVelocity);
		telemetry.addData("Flywheel Actual",shooter.getVelocity());
		telemetry.addData("Increment Index",incrementIndex);
		telemetry.addData("Increment",increments[incrementIndex]);
	}
}
*/