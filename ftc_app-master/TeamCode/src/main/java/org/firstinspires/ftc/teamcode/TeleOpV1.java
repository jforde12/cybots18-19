/*
Copyright 2017 FIRST Tech Challenge Team 5975

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.Arrays;

/**
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * Remove a @Disabled the on the next line or two (if present) to add this opmode to the Driver Station OpMode list,
 * or add a @Disabled annotation to prevent this OpMode from being added to the Driver Station
 */
@TeleOp(name="TeleOpV1", group="TeleOp")

public class TeleOpV1 extends OpMode {
    /* Declare OpMode members. */
    private Gyroscope imu;
    private Gyroscope imu_1;
    private DcMotor PBMotor;
    private DcMotor PFMotor;
    private DcMotor DBMotor;
    private DcMotor DFMotor;
    private DcMotor back;
    private Servo MServo;


    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");
        imu = hardwareMap.get(Gyroscope.class, "imu");
        imu_1 = hardwareMap.get(Gyroscope.class, "imu 1");
        PBMotor = hardwareMap.get(DcMotor.class, "PB");
        PFMotor = hardwareMap.get(DcMotor.class, "PF");
        DBMotor = hardwareMap.get(DcMotor.class, "DB");
        DFMotor = hardwareMap.get(DcMotor.class, "DF");
        back = hardwareMap.get(DcMotor.class, "Back");
        MServo  = hardwareMap.get(Servo.class, "MS");

        DBMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        PFMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        PBMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        DFMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        

    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
        
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        
        
    
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
            
            double leftY = Math.signum(-gamepad1.left_stick_y) * Math.pow(-gamepad1.left_stick_y, 2);
            double leftX = Math.signum(-gamepad1.left_stick_x) * Math.pow(-gamepad1.left_stick_x, 2);
            double rightX = Math.signum(gamepad1.right_stick_x) * Math.pow(gamepad1.right_stick_x, 2);
            
        robotCentric(.6*rightX,.6*-leftX,.6*-leftY);

        if (gamepad1.right_bumper){
            back.setPower(-1);
            
            }else if (gamepad1.left_bumper){
            back.setPower(1);
            
            }else {
                back.setPower(0);
            }

    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

    }
    public void robotCentric(double forwards, double horizontal, double turning) {
        double leftFront = forwards + horizontal + turning;
        double leftBack = forwards - horizontal + turning;
        double rightFront = forwards - horizontal - turning;
        double rightBack = forwards + horizontal - turning;

        double[] wheelPowers = {Math.abs(rightFront), Math.abs(leftFront), Math.abs(leftBack), Math.abs(rightBack)};
        Arrays.sort(wheelPowers);
        double biggestInput = wheelPowers[3];
        if (biggestInput > 1) {
            leftFront /= biggestInput;
            leftBack /= biggestInput;
            rightFront /= biggestInput;
            rightBack /= biggestInput;
        }

        DBMotor.setPower(leftFront);
        PFMotor.setPower(rightFront);
        DFMotor.setPower(leftBack);
        PBMotor.setPower(rightBack);

        telemetry.addData("BackRightMotorPower", PBMotor.getPower());
        telemetry.addData("FrontRightMotorPower", PFMotor.getPower());
        telemetry.addData("FrontLeftRMotorPower", DBMotor.getPower());
        telemetry.addData("BackLeftMotorPower", DFMotor.getPower());
        telemetry.update();
}
}
