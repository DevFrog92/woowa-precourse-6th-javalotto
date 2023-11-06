package lotto.domain.customer;

import static lotto.domain.result.LottoRank.FIFTH;
import static lotto.domain.result.LottoRank.FIRST;
import static lotto.domain.result.LottoRank.FOURTH;
import static lotto.domain.result.LottoRank.SECOND;
import static lotto.domain.result.LottoRank.THIRD;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lotto.domain.checker.Checker;
import lotto.domain.money.Money;
import lotto.domain.result.LottoRank;
import lotto.domain.result.WinResult;
import lotto.domain.seller.Seller;
import lotto.domain.ticket.Ticket;
import lotto.util.Logger;
import lotto.util.reader.InputReader;

/**
 * Customer 를 구현한 개념 객체를 나타내는 클래스입니다.
 */
public class LottoCustomer implements Customer {
        /**
         * 금액을 입력받기 위한 유틸 클래스 입니다.
         */
        private final InputReader inputReader;
        /**
         * 구매한 로또 티켓 입니다.
         */
        private Ticket ticket;

        /**
         * LottoCustomer 의 생성자 함수로 유틸 클래스를 주입받습니다.
         * @param inputReader 입력 유틸 클래금액
         */
        public LottoCustomer(InputReader inputReader) {
                this.inputReader = inputReader;
        }

        /**
         * Seller 를 통해서 티켓을 구매할 수 있습니다.
         * 금액은 유틸 클래스를 통해서 입력받고, Seller 에게 전달합니다.
         * @param seller 판매자
         */
        @Override
        public void buyTicket(Seller seller) {
                Money payment = new Money(new BigDecimal(inputReader.readLine()));
                ticket = seller.sellTo(payment);
        }

        /**
         * 구매한 로또의 결과를 Checker 에게 요청합니다.
         *
         * @param checker 로또 결과 판단자
         */
        @Override
        public void checkLottoResult(Checker checker) {
                WinResult winResult = checker.checkTicket(ticket);
                readResult(winResult);
        }

        /**
         * 반환된 결과를 출력할 수 있습니다.
         *
         * @param result 로또 결과
         */
        private void readResult(WinResult result) {
                Map<LottoRank, Integer> ranks = result.getRanks();
                BigDecimal rateOfReturn = result.getRateOfReturn();
                Logger.info("\n당첨 통계");
                Logger.info("---");

                for (LottoRank rank : List.of(FIFTH, FOURTH, THIRD, SECOND, FIRST)) {
                        Integer value = ranks.get(rank);
                        String priceFormat = String.format("%,d", rank.getPrizes().longValue());
                        Logger.info(rank + " (" + priceFormat + "원) - " + value + "개");
                }

                Logger.info("총 수익률은 " + String.format("%.1f", rateOfReturn) + "%입니다.");
        }
}
