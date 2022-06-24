package edu.epam.bookshop;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BookShopApplicationTests {

	Calculator underTest = new Calculator();

	@Test
	void itShouldAddTwoNumbers() {
		//given
		int numberOne = 10;
		int numberTwo = 30;

		//when
		int result = underTest.add(numberOne, numberTwo);
		int expected = 40;

		//then
		assertThat(result).isEqualTo(expected);
	}

	class Calculator {

		int add(int a, int b) {
			return a + b;
		}
	}
}
