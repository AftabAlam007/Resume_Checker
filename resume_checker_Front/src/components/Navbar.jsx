import { Link, useNavigate } from 'react-router-dom';

const Navbar = () => {
  const navigate = useNavigate();
  const token = localStorage.getItem('token');

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };

  return (
    <nav className="bg-blue-600 text-white p-4">
      <div className="container mx-auto flex justify-between items-center">
        <Link to="/" className="text-xl font-bold">
          Resume Checker
        </Link>
        <div className="space-x-4">
          {token ? (
            <>
              <Link to="/upload" className="hover:text-blue-200">
                Upload Resume
              </Link>
              <Link to="/results" className="hover:text-blue-200">
                Results
              </Link>
              <button
                onClick={handleLogout}
                className="hover:text-blue-200"
              >
                Logout
              </button>
            </>
          ) : (
            <Link to="/login" className="hover:text-blue-200">
              Login
            </Link>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;