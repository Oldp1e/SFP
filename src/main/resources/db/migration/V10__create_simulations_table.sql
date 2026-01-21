-- Create simulations table
CREATE TABLE simulations (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    simulation_type VARCHAR(50) NOT NULL,
    total_value NUMERIC(15, 2) NOT NULL,
    installments INTEGER NOT NULL,
    monthly_value NUMERIC(15, 2) NOT NULL,
    start_date DATE NOT NULL,
    impact_on_balance NUMERIC(15, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_simulations_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX idx_simulations_user_id ON simulations(user_id);
CREATE INDEX idx_simulations_start_date ON simulations(start_date);
