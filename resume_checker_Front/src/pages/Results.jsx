import { useState, useEffect } from 'react';
import { getResults } from '../services/api';

const AccordionItem = ({ title, content, isOpen, onClick }) => (
  <div className="border-t border-gray-200">
    <button
      className="w-full flex justify-between items-center py-4 px-6 text-left text-lg font-medium text-gray-900 focus:outline-none"
      onClick={onClick}
    >
      <span>{title}</span>
      <svg
        className={`w-6 h-6 transform transition-transform ${isOpen ? 'rotate-180' : ''}`}
        fill="none"
        stroke="currentColor"
        viewBox="0 0 24 24"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
      </svg>
    </button>
    {isOpen && (
      <div className="px-6 pb-4">
        <div className="text-sm text-gray-700 whitespace-pre-line">{content}</div>
      </div>
    )}
  </div>
);

const Results = () => {
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [openAccordion, setOpenAccordion] = useState(null);

  useEffect(() => {
    const fetchResults = async () => {
      try {
        const data = await getResults();
        setResults(data);
      } catch (err) {
        setError('Error fetching results. Please try again later.');
      } finally {
        setLoading(false);
      }
    };

    fetchResults();
  }, []);

  const toggleAccordion = (index) => {
    setOpenAccordion(openAccordion === index ? null : index);
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
          <p className="mt-4 text-gray-600">Loading results...</p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center text-red-600">{error}</div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-3xl mx-auto">
        <h2 className="text-3xl font-extrabold text-gray-900 text-center mb-8">
          Resume Analysis Results
        </h2>

        {results.length > 0 ? (
          <div className="bg-white shadow overflow-hidden sm:rounded-lg">
            {results.map((result, index) => (
              <AccordionItem
                key={result.id}
                title={result.fileName}
                content={result.feedback}
                isOpen={openAccordion === index}
                onClick={() => toggleAccordion(index)}
              />
            ))}
          </div>
        ) : (
          <div className="text-center text-gray-600">
            No results available. Please upload a resume first.
          </div>
        )}
      </div>
    </div>
  );
};

export default Results;