// Copyright 2001-2005, Danger, Inc.  All Rights Reserved.
// This file is subject to the Danger, Inc. Sample Code License,
// which is provided in the file SAMPLE_CODE_LICENSE.
// Copies are also available from http://developer.danger.com/

/*
 * The Hiptop2 has some buttons not present in the Hiptop. Also,
 * the Hiptop II OS automatically generates diagonal events for 
 * the D-Pad.
 *
 * This interface makes it possible to handle these events in 
 * applications so that the same jar file will work on both the
 * Hiptop and Hiptop2.
 */

package net.exiva.fido;

public interface Hiptop2Events {
	/**
	 * Send key has been pressed.
	 */
	int DEVICE_BUTTON_SEND = -13;

	/**
	 * End key has been pressed.
	 */
	int DEVICE_BUTTON_END = -14;

	/**
	 * Activity on volume buttons.
	 */
	int DEVICE_BUTTON_VOLUME_UP = -15;
	int DEVICE_BUTTON_VOLUME_DOWN = -16;

	/**
	 * Activity on shoulder buttons.
	 */
	int DEVICE_BUTTON_LEFT_SHOULDER = -17;
	int DEVICE_BUTTON_RIGHT_SHOULDER = -18;

	/**
	 * Activity on CANCEL button.
	 */
	int DEVICE_BUTTON_CANCEL = -20;

	/**
	 * Device joypad has been rocked diagonally up and left.
	 */
	int DEVICE_ARROW_UP_LEFT = -24;

	/**
	 * Device joypad has been rocked diagonally down and left.
	 */
	int DEVICE_ARROW_DOWN_LEFT = -25;

	/**
	 * Device joypad has been rocked diagonally up and right.
	 */
	int DEVICE_ARROW_UP_RIGHT = -26;

	/**
	 * Device joypad has been rocked diagonally down and right.
	 */
	int DEVICE_ARROW_DOWN_RIGHT = -27;
}
