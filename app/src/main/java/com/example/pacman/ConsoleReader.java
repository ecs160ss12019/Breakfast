package com.example.pacman;

import java.io.Console;
import java.util.Scanner;

/*
This is an utility class that reads console
input (keyboard input). The reason we do this is
to use keyboard to control Pacman. Tapping on the screen
of the emulator is simply too slow. Our desire was to
use the direction keys to control, but unfortunately
JVM does not support this kind of input. Thus, we
instead use "WASD" keys to control.
 */
public class ConsoleReader {
    private Console cons;

    public void readCharacter() {
        if(cons == null) {
            System.out.println("Null console");
            return;
        }

        Scanner sc = new Scanner(cons.reader());
        if (sc.hasNext()) {
            System.out.println("input Char: " + sc.next().charAt(0));
        }
    }

    public ConsoleReader(Console cons) {
        this.cons = cons;
    }
}

