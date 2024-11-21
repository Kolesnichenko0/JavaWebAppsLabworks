document.addEventListener('DOMContentLoaded', async () => {
    try {
        const contextPath = document.querySelector('base').href.replace(/\/$/, '');
        const response = await fetch(`${contextPath}/api/users/current`);
        if (!response.ok) {
            throw new Error('Failed to fetch user profile');
        }
        const user = await response.json();

        // Populate profile details
        document.getElementById('profileName').textContent = user.name || '--';
        document.getElementById('profileUsername').textContent = user.username || '--';
        document.getElementById('profileEmail').textContent = user.email || '--';
        document.getElementById('profileRole').textContent = (user.role || '--').toLowerCase();
    } catch (error) {
        console.error('Error fetching user profile:', error);
        // Optional: Display error message to user
        const profileCard = document.querySelector('.card-body');
        profileCard.innerHTML = `
            <div class="alert alert-danger" role="alert">
            Your username has been changed. Please log in again.
            </div>
        `;
    }
});