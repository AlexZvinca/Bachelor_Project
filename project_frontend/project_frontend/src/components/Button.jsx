import React from 'react';

function Button({ label, onClick, type = 'submit' }) {
    return (
        <button type={type} onClick={onClick} className="button">
            {label}
        </button>
    );
}

export default Button;