FROM clojure:latest

RUN mkdir /blackjack
WORKDIR /blackjack
ADD . /blackjack

RUN touch /blackjack/log/blackjack.out.log /blackjack/log/blackjack.err.log
RUN ln -sf /dev/stdout /blackjack/log/blackjack.out.log \
  && ln -sf /dev/stderr /blackjack/log/blackjack.err.log

RUN lein deps

EXPOSE 5000

CMD ["lein", "trampoline", "run", "5000"]
