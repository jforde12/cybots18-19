
package org.firstinspires.ftc.teamcode.subsystems;

import android.speech.tts.TextToSpeech;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.drivebase.Drive;
import org.firstinspires.ftc.teamcode.subsystems.sensors.IMU;

import java.util.Locale;
@Disabled
/**
 * This Robot object is where you can put all robot hardware and common functions
 */
public class Robot {
    //define hardware devices
    public DcMotorEx Motor1, Motor2, Motor3, Motor4;
    public CRServo CRServo1;
    public Servo Servo1;
    public AnalogInput AnalogInput1;
    public ColorSensor Color1;
    public DistanceSensor Distance1;
    public IMU imu;

    //define drivebase
    public Drive drive;

    public LinearOpMode opMode;
    public HardwareMap hwMap;

    public TextToSpeech tts;

    public ElapsedTime runtime  = new ElapsedTime();

    /* Constructor */
    public Robot(LinearOpMode opMode) {
        this.opMode = opMode;
    }

    public void init() {
        hwMap = opMode.hardwareMap;

        //Define and initialize ALL installed servos.
        CRServo1 = hwMap.crservo.get("CRServo1");

        Servo1 = hwMap.servo.get("Servo1");

        Motor1 = (DcMotorEx) hwMap.dcMotor.get("Motor1");
        Motor2 = (DcMotorEx) hwMap.dcMotor.get("Motor2");
        Motor3 = (DcMotorEx) hwMap.dcMotor.get("Motor3");
        Motor4 = (DcMotorEx) hwMap.dcMotor.get("Motor4");

        AnalogInput1 = hwMap.analogInput.get("Analog1");

        Color1 = hwMap.get(ColorSensor.class, "Color1");
        Distance1 = hwMap.get(DistanceSensor.class, "Distance1");

        opMode.telemetry.addData("Hardware","Init Done");
        opMode.telemetry.update();

        //ServoImplEx myServo = (ServoImplEx)hwMap.servo.get("myservo");

        imu = new IMU();
        imu.initIMU(hwMap,"imu");

        opMode.telemetry.addData("IMU","Init Done");
        opMode.telemetry.update();

        //MA3Encoder test = new MA3Encoder(hwMap,"DSe");

        Motor1.setPower(0); //Set Motor1 to 0 power

        Motor1.setDirection(DcMotor.Direction.FORWARD);

        //Set motor run mode
        Motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER); //Set Drive Motor 1 to use encoder

        //Set zero power behavior
        Motor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        drive = new Drive(opMode,imu,Motor1,Motor2,Motor3,Motor4);

        //initialize text-to-speech
        initTTS();

        opMode.telemetry.addData("Full Init","Done");
        opMode.telemetry.update();
    }

    //stop all robot movements
    public void stop(){
        //set all motors to zero power
        Motor1.setPower(0);
    }

    /**
     * Use this method as a timed wait block
     * @param seconds Input seconds for program to wait
     */
    public void pause(double seconds){
        runtime.reset();
        while (runtime.seconds() < seconds&&!opMode.isStopRequested()) {
            //waiting
        }
    }

    /**
     * This method will setup the text-to-speech. Be sure to do this before calling speak()!
     */
    private void initTTS() {
        tts = new TextToSpeech(opMode.hardwareMap.appContext, null);
        tts.setLanguage(Locale.UK);
        tts.setPitch(.5f);
        tts.setSpeechRate(.8f);
    }

    /**
     * Use this method to have the robot speak
     * @param text Text for robot to speak
     */
    public void speak(final String text) {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        }).start();
    }

    /**
     * Here is a thread example
     */
    @Deprecated //I put this deprecated line here because this is just a blank example
    public void startThreadExample() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //put code for thread in here
            }
        }).start();
    }
}