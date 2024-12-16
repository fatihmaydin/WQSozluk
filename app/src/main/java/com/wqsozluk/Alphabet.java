package com.wqsozluk;

import java.util.ArrayList;
import java.util.List;

public class Alphabet {
	public List<Letter> Letters = new ArrayList<Letter>();

	public Alphabet() {
		AddLetter("a", "ئا");
		AddLetter("b", "ب");
		AddLetter("c", "ج");
		AddLetter("ç", "چ");
		AddLetter("d", "د");
		AddLetter("h", "ه");
		AddLetter("e", "ا");
		AddLetter("ê", "ێ");
		AddLetter("f", "ف");
		AddLetter("g", "گ");
		AddLetter("h", "ه");
		AddLetter("h", "ح");
		AddLetter("i", "ی");
		AddLetter("î", "ی");
		AddLetter("j", "ژ");
		AddLetter("k", "ک");
		AddLetter("l", "ل");
		AddLetter("ll", "ڵ");
		AddLetter("m", "م");
		AddLetter("n", "ن");
		AddLetter("o", "ۆ");
		AddLetter("p", "پ");
		AddLetter("q", "ق");
		AddLetter("r", "ر");
		AddLetter("s", "س");
		AddLetter("ş", "ش");
		AddLetter("t", "ت");
		AddLetter("u", "و");
		AddLetter("û", "وو");
		AddLetter("x", "خ");
		AddLetter("v", "ڤ");
		AddLetter("w", "و");
		AddLetter("y", "ی");
		AddLetter("z", "ز");
		AddLetter("0", "٠");
		AddLetter("1", "١");
		AddLetter("2", "٢‎");
		AddLetter("3", "٣‎");
		AddLetter("4", "٤");
		AddLetter("5", "٥‎");
		AddLetter("6", "٦‎");
		AddLetter("7", "٧‎");
		AddLetter("8", "٨‎");
		AddLetter("9", "٩‎");
		
	}

	private void AddLetter(String nav, String beramber) {
		Letter letter = new Letter();
		letter.Nav = nav;
		letter.Beramber = beramber.trim();
		Letters.add(letter);
	}

}
