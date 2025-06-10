import { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate, useLocation } from 'react-router-dom';

function DocumentPage() {
    const [documentUrl, setDocumentUrl] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();
    const location = useLocation();


    const params = new URLSearchParams(location.search);
    const userId = params.get('userId');

    useEffect(() => {
        const fetchDocument = async () => {
            setLoading(true);
            setError(null);
            try {
                const response = await axios.get(`${import.meta.env.VITE_BACKEND_URL}/users/${userId}/id-document`, {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('token')}`,
                        'Content-Type': 'image/jpeg',
                    },
                    responseType: 'arraybuffer',
                });

                const blob = new Blob([response.data], { type: 'image/jpeg' });
                const url = URL.createObjectURL(blob);
                setDocumentUrl(url);
            } catch (err) {
                console.error('Error fetching document:', err);
                setError('Failed to fetch document. Please try again later.');
            } finally {
                setLoading(false);
            }
        };

        fetchDocument();
    }, [userId]);

    return (
        <div style={{ padding: '20px' }}>
            <button onClick={() => navigate("/dashboard")} style={{ marginBottom: '20px' }}>
                Back to Dashboard
            </button>

            {loading && <p>Loading document...</p>}
            {error && <p style={{ color: 'red' }}>{error}</p>}

            {documentUrl && (
                <iframe
                    src={documentUrl}
                    title="ID Document"
                    style={{ width: '100%', height: '80vh', border: '1px solid #ccc' }}
                />
            )}
        </div>
    );
}

export default DocumentPage;