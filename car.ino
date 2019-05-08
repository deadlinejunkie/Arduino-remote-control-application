
/*
 * Arduino Car, Bluetooth remote control
 * Copyright 2019 Anastasiya Sukhotskaya
 * Used libraries: "AFMotor.h" <https://github.com/adafruit/Adafruit-Motor-Shield-library/blob/master/AFMotor.h>
 */

#include <AFMotor.h>

// define motors
AF_DCMotor motorFront();  //  front motor
AF_DCMotor motorRear();   //  rear motor

const byte SPEED_MIN = 100; // minimal motor speed not to stop
const byte SPEED_MAX = 255; // maximum motor speed
byte SPEED_CURRENT = 0; // current momtos speed

// turn direction
const byte ROTATE_RIGHT   = 1;  // right turn
const byte ROTATE_LEFT    = 3;  // left turn

/* pins for module coonection
 * 13, 2 are digital pins
 * 14, 15 are analog pins A0 and A1
 */
 
#define PIN_TRIG 14 //13
#define PIN_ECHO 15 //2

// for bluetooth control
char btAdapter = 'S';

// Counters for bluetooth loose connection
unsigned long btTimer0 = 2000;  // stores the time (in millis since execution started)
unsigned long btTimer1 = 0;     // stores the time when the last command was received from the phone

/******************************************
  Main program
******************************************/

void setup() {
  Serial.begin(9600);
  pinMode(PIN_TRIG, OUTPUT);
  pinMode(PIN_ECHO, INPUT);
  // motorInit();
}

void loop() {
  if (Serial.available() > 0) {
    btTimer1 = millis();
    btCommand = Serial.read(); 
   
    switch (btCommand){
    case 'btnUp':
      motorUp(); // turn the motor on
      break;
    case 'btnStop':
      motorStop(); // turn the motor off
      break;
    case 'btnClutch':
      motorClutch(); // clutch the motor and transmission
      break;
    case 'btnLeft':
      motorRotateLeft(); // turn left
      break;
    case 'btnRight':
      motorRotateRight(); // turn right
      break;
    case 'btnSpeedOne':  
      motorSpeedOne(); // choose speed one
      break;
    case 'btnSpeedTwo':
      motorSpeedTwo(); // choose speed two
      break;
    case 'btnSpeedThree': // choose speed three
      motorSpeedThree();
      break;
    case 'btnSpeedFour':
      motorSpeedFour(); // choose speed four
      break;
    case 'btnSpeedFive': 
      motorSpeedFive(); // choose speed five
      break;
    case 'btnSpeedDown': // choose lower speed
      motorSpeedDown();
      break;
/*      case 'L':  // front lights ON
        break;
      case 'BLON':  // back lights ON
        break;
      case 'BLOFF':  // back lights OFF
        break;
 */
    }
  else{
    btTimer0 = millis();  // get the current time (millis since execution started).
    // check if it has been 500ms since we received last btCommand.
    if ((btTimer0 - btTimer1) > 500)   {
      // more tan 500ms have passed since last btCommand received, car is out of range.
      // therefore stop the car
      motorStop();
    }
  }
}
