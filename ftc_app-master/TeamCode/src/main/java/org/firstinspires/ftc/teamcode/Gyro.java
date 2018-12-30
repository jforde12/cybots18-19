package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name="GyroTest", group="Template")
//@Disabled
public class Gyro extends LinearOpMode {
    //private Robot robot = new Robot(this);
    //private boolean loop = true;
    private DcMotor lift;
    private DcMotor PBMotor;
    private DcMotor PFMotor;
    private DcMotor DBMotor;
    private DcMotor DFMotor;
    private IMU imu;

    @Override
    public void runOpMode() {
        PBMotor = hardwareMap.get(DcMotor.class, "PB");
        PFMotor = hardwareMap.get(DcMotor.class, "PF");
        DBMotor = hardwareMap.get(DcMotor.class, "DB");
        DFMotor = hardwareMap.get(DcMotor.class, "DF");
        lift  = hardwareMap.get(DcMotor.class, "Back");

        imu = new IMU();

        imu.initIMU(hardwareMap, "imu");


        DBMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        DFMotor.setDirection(DcMotorSimple.Direction.REVERSE);


        //robot.init();

        //AutoTransitioner.transitionOnStop(this, "Teleop V1");

        waitForStart();




        while (!isStopRequested()) {
            telemetry.addData("angle", imu.getHeading());
            telemetry.update();
        }
    }
}
