package org.firstinspires.ftc.teamcode.matchCode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystems.Robot;

@Autonomous(name="Template Auto", group="Template")
@Disabled
public class BaseAutoTemplate extends LinearOpMode {
    private Robot robot = new Robot(this);
    private boolean loop = true;

    @Override
    public void runOpMode() {
        robot.init();

        //AutoTransitioner.transitionOnStop(this, "Teleop V1");

        waitForStart();
        //put autonomous code blocks here
        robot.drive.timeDrive(.1,0,0,1);

    }
}
