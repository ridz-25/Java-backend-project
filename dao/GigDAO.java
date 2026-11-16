package dao;

import model.Gig;
import java.util.List;
import java.util.Optional;

public interface GigDAO {

    boolean saveGig(Gig gig);

    Optional<Gig> getGigById(int gigId);

    List<Gig> getAllGigs();

    List<Gig> getGigsBySeller(int sellerId);

    List<Gig> getGigsByCategory(int categoryId);

    List<Gig> searchGigs(String keyword);

    boolean updateGig(Gig gig);

    boolean deleteGig(int gigId);
}