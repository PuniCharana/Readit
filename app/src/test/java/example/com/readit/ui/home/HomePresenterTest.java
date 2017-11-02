package example.com.readit.ui.home;

import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Created by FamilyPC on 10/24/2017.
 */
public class HomePresenterTest {

    private HomePresenter mHomePresenter;
    private HomeContract.HomeView mHomeView;
    View view;

    @Before
    public void setUp() throws Exception {
        mHomeView = Mockito.mock(HomeContract.HomeView.class);

        mHomePresenter = new HomePresenter(mHomeView);
        view = Mockito.mock(View.class);
    }

    @Test
    public void onFabClicked() throws Exception {
        mHomePresenter.onFabClicked(view);
        Mockito.verify(mHomeView).showFabClicked();
    }

    @Test
    public void onNavigationItemSelected() throws Exception {

    }

    @Test
    public void onOptionsItemSelected() throws Exception {

    }

}