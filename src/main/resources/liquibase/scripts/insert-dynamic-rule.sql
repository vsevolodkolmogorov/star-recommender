CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Вероятно есть ошибки, не стал применять код.

-- Вставка записи в таблицу dynamic_rule с авто-генерацией UUID
INSERT INTO dynamic_rule (id, product_name, product_id, product_text)
VALUES (uuid_generate_v4(), 'Простой кредит', uuid_generate_v4(), 'Откройте мир выгодных кредитов с нами!Ищете способ быстро и без лишних хлопот получить нужную сумму? Тогда наш выгодный кредит — именно то, что вам нужно! ' ||
                                                                  'Мы предлагаем низкие процентные ставки, гибкие условия и индивидуальный подход к каждому клиенту.Почему выбирают нас:Быстрое рассмотрение заявки. Мы ценим ваше время, поэтому процесс рассмотрения заявки занимает всего несколько часов.Удобное оформление. ' ||
                                                                  'Подать заявку на кредит можно онлайн на нашем сайте или в мобильном приложении.Широкий выбор кредитных продуктов.' ||
                                                                  ' Мы предлагаем кредиты на различные цели: покупку недвижимости, автомобиля, образование, лечение и многое другое.Не упустите возможность воспользоваться выгодными условиями кредитования от нашей компании!');

INSERT INTO dynamic_rule (id, product_name, product_id, product_text)
VALUES (uuid_generate_v4(), 'Top Saving', uuid_generate_v4(), 'Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели. Больше никаких забытых чеков и потерянных квитанций — всё под контролем!Преимущества «Копилки»:Накопление средств на конкретные цели. ' ||
                                                              'Установите лимит и срок накопления, и банк будет автоматически переводить определенную сумму на ваш счет.Прозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс накопления и корректируйте стратегию при необходимости.Безопасность и надежность. ' ||
                                                              'Ваши средства находятся под защитой банка, а доступ к ним возможен только через мобильное приложение или интернет-банкинг.Начните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!');

INSERT INTO dynamic_rule (id, product_name, product_id, product_text)
VALUES (uuid_generate_v4(), 'Invest 500', uuid_generate_v4(), 'Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! Воспользуйтесь налоговыми льготами и начните инвестировать с умом. ' ||
                                                              'Пополните счет до конца года и получите выгоду в виде вычета на взнос в следующем налоговом периоде. ' ||
                                                              'Не упустите возможность разнообразить свой портфель, снизить риски и следить за актуальными рыночными тенденциями. Откройте ИИС сегодня и станьте ближе к финансовой независимости!');

-- Вставка записей в таблицу rule с авто-генерацией UUID
INSERT INTO rule (rule_id, query, negate, dynamic_rule_id)
VALUES
    (uuid_generate_v4(), 'USER_OF', TRUE, (SELECT id FROM dynamic_rule WHERE product_name = 'Простой кредит')),
    (uuid_generate_v4(), 'TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW', FALSE, (SELECT id FROM dynamic_rule WHERE product_name = 'Простой кредит')),
    (uuid_generate_v4(), 'TRANSACTION_SUM_COMPARE', FALSE, (SELECT id FROM dynamic_rule WHERE product_name = 'Простой кредит'));

INSERT INTO rule (rule_id, query, negate, dynamic_rule_id)
VALUES
    (uuid_generate_v4(), 'USER_OF', FALSE, (SELECT id FROM dynamic_rule WHERE product_name = 'Invest 500')),
    (uuid_generate_v4(), 'USER_OF', TRUE, (SELECT id FROM dynamic_rule WHERE product_name = 'Invest 500')),
    (uuid_generate_v4(), 'TRANSACTION_SUM_COMPARE', FALSE, (SELECT id FROM dynamic_rule WHERE product_name = 'Invest 500'));

INSERT INTO rule (rule_id, query, negate, dynamic_rule_id)
VALUES
    (uuid_generate_v4(), 'USER_OF', FALSE, (SELECT id FROM dynamic_rule WHERE product_name = 'Top Saving')),
    (uuid_generate_v4(), 'TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW', FALSE, (SELECT id FROM dynamic_rule WHERE product_name = 'Top Saving')),
    (uuid_generate_v4(), 'TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW', FALSE, (SELECT id FROM dynamic_rule WHERE product_name = 'Top Saving')),

-- Вставка записей в таблицу argument с авто-генерацией UUID
INSERT INTO argument (argument_id, rule_id, text)
VALUES
    (uuid_generate_v4(), (SELECT rule_id FROM rule WHERE query = 'USER_OF'), 'CREDIT'),
    (uuid_generate_v4(), (SELECT rule_id FROM rule WHERE query = 'TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW'), 'DEBIT'),
    (uuid_generate_v4(), (SELECT rule_id FROM rule WHERE query = 'TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW'), '>'),
    (uuid_generate_v4(), (SELECT rule_id FROM rule WHERE query = 'TRANSACTION_SUM_COMPARE'), 'DEBIT'),
    (uuid_generate_v4(), (SELECT rule_id FROM rule WHERE query = 'TRANSACTION_SUM_COMPARE'), 'DEPOSIT'),
    (uuid_generate_v4(), (SELECT rule_id FROM rule WHERE query = 'TRANSACTION_SUM_COMPARE'), '>'),
    (uuid_generate_v4(), (SELECT rule_id FROM rule WHERE query = 'TRANSACTION_SUM_COMPARE'), '100000');


INSERT INTO argument (argument_id, rule_id, text)
VALUES
    (uuid_generate_v4(), (SELECT rule_id FROM rule WHERE query = 'USER_OF'), 'DEBIT'),
    (uuid_generate_v4(), (SELECT rule_id FROM rule WHERE query = 'USER_OF'), 'CREDIT'),
    (uuid_generate_v4(), (SELECT rule_id FROM rule WHERE query = 'TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW'), 'SAVING'),
    (uuid_generate_v4(), (SELECT rule_id FROM rule WHERE query = 'TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW'), '<'),


INSERT INTO argument (argument_id, rule_id, text)
VALUES
    (uuid_generate_v4(), (SELECT rule_id FROM rule WHERE query = 'USER_OF'), 'DEBIT'),
    (uuid_generate_v4(), (SELECT rule_id FROM rule WHERE query = 'TRANSACTION_SUM_COMPARE'), 'SAVING'),
    (uuid_generate_v4(), (SELECT rule_id FROM rule WHERE query = 'TRANSACTION_SUM_COMPARE'), 'DEPOSIT'),
    (uuid_generate_v4(), (SELECT rule_id FROM rule WHERE query = 'TRANSACTION_SUM_COMPARE'), '>'),
    (uuid_generate_v4(), (SELECT rule_id FROM rule WHERE query = 'TRANSACTION_SUM_COMPARE'), '50000');
    (uuid_generate_v4(), (SELECT rule_id FROM rule WHERE query = 'TRANSACTION_SUM_COMPARE'), 'DEBIT'),
    (uuid_generate_v4(), (SELECT rule_id FROM rule WHERE query = 'TRANSACTION_SUM_COMPARE'), 'DEPOSIT'),
    (uuid_generate_v4(), (SELECT rule_id FROM rule WHERE query = 'TRANSACTION_SUM_COMPARE'), '>'),
    (uuid_generate_v4(), (SELECT rule_id FROM rule WHERE query = 'TRANSACTION_SUM_COMPARE'), '50000');
    (uuid_generate_v4(), (SELECT rule_id FROM rule WHERE query = 'TRANSACTION_SUM_COMPARE'), 'DEBIT'),
    (uuid_generate_v4(), (SELECT rule_id FROM rule WHERE query = 'TRANSACTION_SUM_COMPARE'), 'DEPOSIT'),
    (uuid_generate_v4(), (SELECT rule_id FROM rule WHERE query = 'TRANSACTION_SUM_COMPARE'), '>'),
    (uuid_generate_v4(), (SELECT rule_id FROM rule WHERE query = 'TRANSACTION_SUM_COMPARE'), '100000');
