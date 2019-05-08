
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
const byte TURN_RIGHT   = 1;  // right turn
const byte TURN_LEFT    = 3;  // left turn

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
  
 // motorStop();
