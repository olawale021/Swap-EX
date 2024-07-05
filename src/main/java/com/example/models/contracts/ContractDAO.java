package com.example.models.contracts;

import com.example.models.contracts.ContractModel;
import com.example.models.messages.MessageDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ContractDAO {
    private final Connection connection;

    public ContractDAO(Connection connection) {
        this.connection = connection;
    }
    private static final Logger LOGGER = Logger.getLogger(MessageDAO.class.getName());

    public void createContract(ContractModel contract) throws SQLException {
        String query = "INSERT INTO contracts (exchange_id, terms, signed_by_owner, signed_by_interested_user, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, contract.getExchangeId());
            stmt.setString(2, contract.getTerms());
            stmt.setBoolean(3, contract.isSignedByOwner());
            stmt.setBoolean(4, contract.isSignedByInterestedUser());
            stmt.setTimestamp(5, contract.getCreatedAt());
            stmt.setTimestamp(6, contract.getUpdatedAt());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        contract.setId(generatedKeys.getInt(1));
                    }
                }
            }
        }
    }

    public ContractModel getContractById(int id) throws SQLException {
        String query = "SELECT * FROM contracts WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToContract(rs);
                }
            }
        }
        return null;
    }

    public List<ContractModel> getAllContracts() throws SQLException {
        List<ContractModel> contracts = new ArrayList<>();
        String query = "SELECT * FROM contracts";
        try (PreparedStatement stmt = connection.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                contracts.add(mapRowToContract(rs));
            }
        }
        return contracts;
    }

    public ContractModel getContractByExchangeId(int exchangeId) throws SQLException {
        String query = "SELECT * FROM contracts WHERE exchange_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, exchangeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToContract(rs);
                }
            }
        }
        return null;
    }


    public void updateContract(ContractModel contract) throws SQLException {
        String query = "UPDATE contracts SET exchange_id = ?, terms = ?, signed_by_owner = ?, signed_by_interested_user = ?, updated_at = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, contract.getExchangeId());
            stmt.setString(2, contract.getTerms());
            stmt.setBoolean(3, contract.isSignedByOwner());
            stmt.setBoolean(4, contract.isSignedByInterestedUser());
            stmt.setTimestamp(5, contract.getUpdatedAt());
            stmt.setInt(6, contract.getId());

            LOGGER.info("Executing update for contract: " + contract);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating contract failed, no rows affected.");
            }
        }
    }


    public void deleteContract(int id) throws SQLException {
        String query = "DELETE FROM contracts WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void updateExchangeStatus(int exchangeId, String status) throws SQLException {
        String query = "UPDATE exchanges SET status = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setInt(2, exchangeId);
            stmt.executeUpdate();
        }
    }

    private ContractModel mapRowToContract(ResultSet rs) throws SQLException {
        ContractModel contract = new ContractModel();
        contract.setId(rs.getInt("id"));
        contract.setExchangeId(rs.getInt("exchange_id"));
        contract.setTerms(rs.getString("terms"));
        contract.setSignedByOwner(rs.getBoolean("signed_by_owner"));
        contract.setSignedByInterestedUser(rs.getBoolean("signed_by_interested_user"));
        contract.setCreatedAt(rs.getTimestamp("created_at"));
        contract.setUpdatedAt(rs.getTimestamp("updated_at"));
        return contract;
    }
}
