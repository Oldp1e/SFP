-- Create credit_card_transactions table
CREATE TABLE credit_card_transactions (
    id UUID PRIMARY KEY,
    credit_card_id UUID NOT NULL,
    user_id UUID NOT NULL,
    description VARCHAR(255) NOT NULL,
    total_amount NUMERIC(15, 2) NOT NULL,
    installments INTEGER NOT NULL,
    installment_value NUMERIC(15, 2) NOT NULL,
    current_installment INTEGER NOT NULL,
    purchase_date DATE NOT NULL,
    first_due_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_cct_credit_card_id FOREIGN KEY (credit_card_id) REFERENCES credit_cards(id) ON DELETE CASCADE,
    CONSTRAINT fk_cct_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX idx_cct_credit_card_id ON credit_card_transactions(credit_card_id);
CREATE INDEX idx_cct_user_id ON credit_card_transactions(user_id);
CREATE INDEX idx_cct_purchase_date ON credit_card_transactions(purchase_date);
CREATE INDEX idx_cct_first_due_date ON credit_card_transactions(first_due_date);
