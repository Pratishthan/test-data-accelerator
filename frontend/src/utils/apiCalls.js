import urlConfig from "../consts/url-config";


const postData = async (type, data) => {
  try {
    const response = await fetch(urlConfig[type], {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }
    
    return response;
  } catch (error) {
    console.log(error);
    window.alert('Failed:', data);
    throw error;
  }
};

export default { postData };
