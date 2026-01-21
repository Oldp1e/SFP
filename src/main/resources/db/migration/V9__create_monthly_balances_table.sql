-- Create monthly_balances table (denormalized balance aggregates)
CREATE TABLE monthly_balances (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    reference_month INTEGER NOT NULL,
    reference_year INTEGER NOT NULL,
    total_income NUMERIC(15, 2) NOT NULL,
    total_expense NUMERIC(15, 2) NOT NULL,
    balance NUMERIC(15, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_monthly_balances_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uk_monthly_balances_user_month_year UNIQUE (user_id, reference_month, reference_year)
);

-- Create indexes
CREATE INDEX idx_monthly_balances_user_id ON monthly_balances(user_id);
CREATE INDEX idx_monthly_balances_year_month ON monthly_balances(reference_year, reference_month);
