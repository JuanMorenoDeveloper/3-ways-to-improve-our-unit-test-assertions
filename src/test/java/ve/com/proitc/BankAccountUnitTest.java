package ve.com.proitc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

class BankAccountUnitTest {

  @Test
  void givenAccountList_whenTakeWhile_thenSizeAndSumValues() {
    var accounts = List
        .of(new BankAccount(100), new BankAccount(200), new BankAccount(300), new BankAccount(400),
            new BankAccount(500));

    //Select balances lower than 250 = 100 and 200
    var filteredAccounts = accounts
        .stream()
        .takeWhile(account -> account.getBalance() < 250)
        .collect(Collectors.toList());

    SoftAssertions.assertSoftly(softly -> {
      softly
          .assertThat(filteredAccounts)
          .hasSize(2);
      softly
          .assertThat(filteredAccounts
              .stream()
              .mapToDouble(BankAccount::getBalance)
              .sum())
          .isEqualTo(300, Offset.offset(0.1));
      softly.assertAll();
    });
  }

  @Test
  void givenAccountList_whenDropWhile_thenSizeAndSumValues() {
    var accounts = List
        .of(new BankAccount(100), new BankAccount(200), new BankAccount(300), new BankAccount(400),
            new BankAccount(500));

    //Select balances equal or bigger than 250 = 300, 400 and 500
    var filteredAccounts = accounts
        .stream()
        .dropWhile(account -> account.getBalance() < 250)
        .collect(Collectors.toList());

    SoftAssertions.assertSoftly(softly -> {
      softly
          .assertThat(filteredAccounts)
          .hasSize(3);
      softly
          .assertThat(filteredAccounts
              .stream()
              .mapToDouble(BankAccount::getBalance)
              .sum())
          .isEqualTo(1200, Offset.offset(0.1));
      softly.assertAll();
    });
  }

  @Test
  void whenIterateStream_thenExpectedList() {
    var accounts = Stream
        .iterate(new BankAccount(100), account -> account.getBalance() <= 500,
            account -> new BankAccount(account.getBalance() + 100))
        .collect(Collectors.toList());

    SoftAssertions.assertSoftly(softly -> {
      softly
          .assertThat(accounts)
          .hasSize(5);
      softly
          .assertThat(accounts
              .stream()
              .mapToDouble(BankAccount::getBalance)
              .sum())
          .isEqualTo(1500, Offset.offset(0.1));
      softly.assertAll();
    });
  }

  @Test
  void whenFilterListByLimit_thenGetFilteredAmount() {
    int limit = 400;
    var accounts = List
        .of(new BankAccount(100), new BankAccount(200), new BankAccount(300), new BankAccount(400),
            new BankAccount(500));

    double amountFiltered = accounts
        .stream()
        .filter(account -> account.getBalance() >= limit)
        .findAny()
        .stream()
        .mapToDouble(BankAccount::getBalance)
        .sum();

    assertThat(amountFiltered).isGreaterThanOrEqualTo(limit);
  }

  @Test
  void whenFilterListByUpperLimit_thenDefaultAccount() {
    int limit = 600;
    var defaultAmount = Optional.of(new BankAccount(0));
    var accounts = List
        .of(new BankAccount(100), new BankAccount(200), new BankAccount(300), new BankAccount(400),
            new BankAccount(500));

    double amountFiltered = accounts
        .stream()
        .filter(account -> account.getBalance() >= limit)
        .findAny()
        .or(() -> defaultAmount)
        .stream()
        .mapToDouble(BankAccount::getBalance)
        .sum();

    assertThat(amountFiltered).isZero();
  }

  @Test
  void whenFilterListByUpperLimitWithOptionals_thenDefaultBalance()
      throws IllegalArgumentException {
    int limit = 600;
    double defaultBalance = 0;
    var defaultAccount = new BankAccount(defaultBalance);
    var accounts = List
        .of(new BankAccount(100), new BankAccount(200), new BankAccount(300), new BankAccount(400),
            new BankAccount(500));
    var result = new ArrayList<BankAccount>();

    accounts
        .stream()
        .filter(account -> account.getBalance() >= limit)
        .findAny()
        .ifPresentOrElse(result::add, () -> result.add(defaultAccount));

    assertThat(result)
        .hasSize(1)
        .contains(defaultAccount);
  }

  @Test
  void whenFilterListByLimit_thenFilteredList() {
    int limit = 300;
    var accounts = List
        .of(new BankAccount(100), new BankAccount(200),
            new BankAccount(300), new BankAccount(400),
            new BankAccount(500));

    var result = accounts
        .stream()
        .filter(account -> account.getBalance() >= limit)
        .collect(Collectors.toList());

    assertEquals(result.size(), 3);
    assertEquals(3, result.size());

    org.hamcrest.MatcherAssert.assertThat(result, hasSize(3));

    assertThat(result)
        .hasSize(3)
        .doesNotContain(new BankAccount(100));
  }
}

